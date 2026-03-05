DESTINATION_DIRECTORY := src/main/resources
JWT_PRIVATE_KEY_DESTINATION := $(DESTINATION_DIRECTORY)/jwt-private-key.pem
JWT_PUBLIC_KEY_DESTINATION  := $(DESTINATION_DIRECTORY)/jwt-public-key.pem

JAR_OUTPUT_DIRECTORY := build/libs
APP_NAME := recipe-catalogue
DEPLOY_DESTINATION := /opt/recipe-catalogue
JAR_DESTINATION := $(DEPLOY_DESTINATION)/app.jar
SERVICE_NAME := recipe-catalogue
STAGING_SERVICE_DESTINATION := /etc/systemd/system/$(SERVICE_NAME)-staging.service
PROD_SERVICE_DESTINATION := /etc/systemd/system/$(SERVICE_NAME)-prod.service

.PHONY: list create-jwt-keys overwrite-jwt-keys clean-jwt-keys show-jwt-keys build-jar deploy-jar restart-staging restart-prod stop-service status journal-staging journal-prod create-staging-service create-prod-service deploy-staging deploy-prod

list:
	@echo "Available targets:"
	@echo "  make list                   # show available targets"
	@echo "  make create-jwt-keys        # generate JWT EC P-384 key pair"
	@echo "  make overwrite-jwt-keys     # overwrite JWT key pair"
	@echo "  make clean-jwt-keys         # delete JWT key pair"
	@echo "  make show-jwt-keys          # show JWT key file status"
	@echo "  make build-jar              # build Spring Boot jar"
	@echo "  make deploy-jar             # copy newest jar to deployment directory"
	@echo "  make create-staging-service # create/overwrite systemd unit for staging"
	@echo "  make create-prod-service    # create/overwrite systemd unit for prod"
	@echo "  make restart-staging        # restart staging service"
	@echo "  make restart-prod           # restart prod service"
	@echo "  make stop-service           # stop both staging and prod services"
	@echo "  make status                 # show status for both services"
	@echo "  make journal-staging        # tail staging service logs"
	@echo "  make journal-prod           # tail prod service logs"
	@echo "  make deploy-staging         # build, deploy, restart staging"
	@echo "  make deploy-prod            # build, deploy, restart prod"

create-jwt-keys:
	@mkdir -p "$(DESTINATION_DIRECTORY)"
	@if [ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] || [ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]; then \
		echo "Keys already exist. Refusing to overwrite."; \
		[ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] && echo "  - $(JWT_PRIVATE_KEY_DESTINATION)"; \
		[ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]  && echo "  - $(JWT_PUBLIC_KEY_DESTINATION)"; \
		echo "Run: make overwrite-jwt-keys  (or make clean-jwt-keys)"; \
		exit 1; \
	fi
	@echo "Generating EC P-384 private key -> $(JWT_PRIVATE_KEY_DESTINATION)"
	openssl ecparam -name secp384r1 -genkey -noout -out "$(JWT_PRIVATE_KEY_DESTINATION)"
	@echo "Deriving public key -> $(JWT_PUBLIC_KEY_DESTINATION)"
	openssl ec -in "$(JWT_PRIVATE_KEY_DESTINATION)" -pubout -out "$(JWT_PUBLIC_KEY_DESTINATION)"
	@chmod 600 "$(JWT_PRIVATE_KEY_DESTINATION)" 2>/dev/null || true
	@chmod 644 "$(JWT_PUBLIC_KEY_DESTINATION)" 2>/dev/null || true
	@echo "Finished generating keys."

overwrite-jwt-keys: clean-jwt-keys create-jwt-keys

clean-jwt-keys:
	@rm -f "$(JWT_PRIVATE_KEY_DESTINATION)" "$(JWT_PUBLIC_KEY_DESTINATION)"
	@echo "Deleted:"
	@echo "  - $(JWT_PRIVATE_KEY_DESTINATION)"
	@echo "  - $(JWT_PUBLIC_KEY_DESTINATION)"

show-jwt-keys:
	@echo "Private: $(JWT_PRIVATE_KEY_DESTINATION) $$( [ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] && echo '(exists)' || echo '(missing)' )"
	@echo "Public : $(JWT_PUBLIC_KEY_DESTINATION)  $$( [ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]  && echo '(exists)' || echo '(missing)' )"

build-jar:
	@echo "Building Spring Boot jar..."
	@./gradlew clean bootJar

deploy-jar:
	@echo "Deploying newest jar to $(JAR_DESTINATION)..."
	@JAR_FILE=$$(ls -1t $(JAR_OUTPUT_DIRECTORY)/*.jar | head -n 1); \
	if [ -z "$$JAR_FILE" ]; then \
		echo "No jar found in $(JAR_OUTPUT_DIRECTORY). Run 'make build-jar' first."; \
		exit 1; \
	fi; \
	echo "Deploying $$JAR_FILE -> $(JAR_DESTINATION)"; \
	sudo mkdir -p "$(DEPLOY_DESTINATION)"; \
	sudo cp "$$JAR_FILE" "$(JAR_DESTINATION)"; \
	sudo chown -R $$USER:$$USER "$(DEPLOY_DESTINATION)"

restart-staging:
	@echo "Restarting $(SERVICE_NAME)-staging..."
	@sudo systemctl daemon-reload
	@sudo systemctl restart $(SERVICE_NAME)-staging
	@sudo systemctl status $(SERVICE_NAME)-staging --no-pager

restart-prod:
	@echo "Restarting $(SERVICE_NAME)-prod..."
	@sudo systemctl daemon-reload
	@sudo systemctl restart $(SERVICE_NAME)-prod
	@sudo systemctl status $(SERVICE_NAME)-prod --no-pager

stop-service:
	@echo "Stopping $(SERVICE_NAME)-staging and $(SERVICE_NAME)-prod (if running)..."
	@sudo systemctl stop $(SERVICE_NAME)-staging || true
	@sudo systemctl stop $(SERVICE_NAME)-prod || true
	@sudo systemctl status $(SERVICE_NAME)-staging --no-pager || true
	@sudo systemctl status $(SERVICE_NAME)-prod --no-pager || true

status:
	@echo "Service status:"
	@sudo systemctl status $(SERVICE_NAME)-staging --no-pager || true
	@sudo systemctl status $(SERVICE_NAME)-prod --no-pager || true

journal-staging:
	@echo "Tailing logs for $(SERVICE_NAME)-staging (Ctrl+C to stop)..."
	@sudo journalctl -u $(SERVICE_NAME)-staging -f

journal-prod:
	@echo "Tailing logs for $(SERVICE_NAME)-prod (Ctrl+C to stop)..."
	@sudo journalctl -u $(SERVICE_NAME)-prod -f

create-staging-service:
	@if [ -f "$(STAGING_SERVICE_DESTINATION)" ]; then echo "Overwriting $(STAGING_SERVICE_DESTINATION)"; else echo "Creating $(STAGING_SERVICE_DESTINATION)"; fi
	@printf '%s\n' \
		'[Unit]' \
		'Description=Recipe Catalogue API (staging)' \
		'After=network.target docker.service' \
		'Requires=docker.service' \
		'' \
		'[Service]' \
		'User=ubuntu' \
		'WorkingDirectory=$(DEPLOY_DESTINATION)' \
		'Environment="SPRING_PROFILES_ACTIVE=staging"' \
		'ExecStart=/usr/bin/java -jar $(JAR_DESTINATION)' \
		'Restart=always' \
		'RestartSec=5' \
		'' \
		'[Install]' \
		'WantedBy=multi-user.target' \
	| sudo tee "$(STAGING_SERVICE_DESTINATION)" >/dev/null
	@echo "Created $(STAGING_SERVICE_DESTINATION)"
	@sudo systemctl daemon-reload

create-prod-service:
	@if [ -f "$(PROD_SERVICE_DESTINATION)" ]; then echo "Overwriting $(PROD_SERVICE_DESTINATION)"; else echo "Creating $(PROD_SERVICE_DESTINATION)"; fi
	@printf '%s\n' \
		'[Unit]' \
		'Description=Recipe Catalogue API (prod)' \
		'After=network.target docker.service' \
		'Requires=docker.service' \
		'' \
		'[Service]' \
		'User=ubuntu' \
		'WorkingDirectory=$(DEPLOY_DESTINATION)' \
		'Environment="SPRING_PROFILES_ACTIVE=prod"' \
		'ExecStart=/usr/bin/java -jar $(JAR_DESTINATION)' \
		'Restart=always' \
		'RestartSec=5' \
		'' \
		'[Install]' \
		'WantedBy=multi-user.target' \
	| sudo tee "$(PROD_SERVICE_DESTINATION)" >/dev/null
	@echo "Created $(PROD_SERVICE_DESTINATION)"
	@sudo systemctl daemon-reload

deploy-staging: build-jar deploy-jar restart-staging

deploy-prod: build-jar deploy-jar restart-prod
