DESTINATION_DIRECTORY := src/main/resources
JWT_PRIVATE_KEY_DESTINATION := $(DESTINATION_DIRECTORY)/jwt-private-key.pem
JWT_PUBLIC_KEY_DESTINATION  := $(DESTINATION_DIRECTORY)/jwt-public-key.pem

.PHONY: create overwrite clean show

create:
	@mkdir -p "$(DESTINATION_DIRECTORY)"
	@if [ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] || [ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]; then \
		echo "Keys already exist. Refusing to overwrite."; \
		[ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] && echo "  - $(JWT_PRIVATE_KEY_DESTINATION)"; \
		[ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]  && echo "  - $(JWT_PUBLIC_KEY_DESTINATION)"; \
		echo "Run: make overwrite  (or make clean)"; \
		exit 1; \
	fi
	@echo "Generating EC P-384 private key -> $(JWT_PRIVATE_KEY_DESTINATION)"
	openssl ecparam -name secp384r1 -genkey -noout -out "$(JWT_PRIVATE_KEY_DESTINATION)"
	@echo "Deriving public key -> $(JWT_PUBLIC_KEY_DESTINATION)"
	openssl ec -in "$(JWT_PRIVATE_KEY_DESTINATION)" -pubout -out "$(JWT_PUBLIC_KEY_DESTINATION)"
	@chmod 600 "$(JWT_PRIVATE_KEY_DESTINATION)" 2>/dev/null || true
	@chmod 644 "$(JWT_PUBLIC_KEY_DESTINATION)" 2>/dev/null || true
	@echo "Finished generating keys."

overwrite: clean create

clean:
	@rm -f "$(JWT_PRIVATE_KEY_DESTINATION)" "$(JWT_PUBLIC_KEY_DESTINATION)"
	@echo "Deleted:"
	@echo "  - $(JWT_PRIVATE_KEY_DESTINATION)"
	@echo "  - $(JWT_PUBLIC_KEY_DESTINATION)"

show:
	@echo "Private: $(JWT_PRIVATE_KEY_DESTINATION) $$( [ -f "$(JWT_PRIVATE_KEY_DESTINATION)" ] && echo '(exists)' || echo '(missing)' )"
	@echo "Public : $(JWT_PUBLIC_KEY_DESTINATION)  $$( [ -f "$(JWT_PUBLIC_KEY_DESTINATION)" ]  && echo '(exists)' || echo '(missing)' )"
