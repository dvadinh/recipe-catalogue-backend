INSERT INTO steps (id, section_id, number, title, description)
VALUES
  -- Section 1: Caramelize Onions (Recipe 1: Shakshuka)
  (1, 1, 1, 'Slice Onions Thinly', 'Slice 4 large yellow onions into thin half-moons for even caramelization.'),
  (2, 1, 2, 'Cook Low and Slow', 'Cook onions in butter over medium-low heat for 45-60 minutes, stirring occasionally.'),
  (3, 1, 3, 'Add Sugar and Wine', 'Sprinkle with 1 tsp sugar, deglaze with 1/4 cup white wine.'),

  -- Section 2: Build the Soup (Recipe 1: Shakshuka)
  (4, 2, 1, 'Add Broth', 'Pour in 8 cups beef broth and bring to a simmer.'),
  (5, 2, 2, 'Season', 'Add thyme, bay leaf, salt, and pepper to taste.'),
  (6, 2, 3, 'Simmer', 'Simmer for 30 minutes to blend flavors.'),

  -- Section 3: Gratinée and Serve (Recipe 1: Shakshuka)
  (7, 3, 1, 'Toast Bread', 'Toast baguette slices until golden and crispy.'),
  (8, 3, 2, 'Assemble', 'Ladle soup into oven-safe bowls, top with bread and grated gruyere.'),
  (9, 3, 3, 'Broil', 'Broil until cheese is melted, bubbly, and golden brown.'),

  -- Section 4: Make Dressing (Recipe 2: Grandma's Secret Meatballs - PRIVATE)
  (10, 4, 1, 'Combine Base', 'Whisk together 2 egg yolks, 2 minced garlic cloves, 4 minced anchovies.'),
  (11, 4, 2, 'Add Acid', 'Whisk in 2 tbsp lemon juice and 1 tsp Dijon mustard.'),
  (12, 4, 3, 'Emulsify Oil', 'Slowly drizzle in 3/4 cup olive oil while whisking constantly until creamy.'),

  -- Section 5: Prepare Croutons (Recipe 2: Grandma's Secret Meatballs - PRIVATE)
  (13, 5, 1, 'Cut Bread', 'Cut day-old bread into 1-inch cubes.'),
  (14, 5, 2, 'Season', 'Toss with olive oil, minced garlic, salt, and pepper.'),
  (15, 5, 3, 'Bake', 'Bake at 375°F for 12-15 minutes until golden and crispy.'),

  -- Section 6: Assemble Salad (Recipe 2: Grandma's Secret Meatballs - PRIVATE)
  (16, 6, 1, 'Prepare Lettuce', 'Wash and dry 2 heads romaine lettuce, tear into bite-sized pieces.'),
  (17, 6, 2, 'Toss', 'Toss lettuce with dressing until evenly coated.'),
  (18, 6, 3, 'Garnish', 'Top with croutons and freshly shaved parmesan cheese.'),

  -- Section 7: Prepare Beef and Duxelles (Recipe 3: Spaghetti Carbonara)
  (19, 7, 1, 'Sear Beef', 'Sear 2 lb beef tenderloin in hot oil until browned on all sides, about 2 minutes per side.'),
  (20, 7, 2, 'Make Duxelles', 'Sauté 1 lb finely chopped mushrooms with shallots until dry, season with thyme.'),
  (21, 7, 3, 'Cool Both', 'Let beef and mushroom mixture cool completely.'),

  -- Section 8: Wrap in Pastry (Recipe 3: Spaghetti Carbonara)
  (22, 8, 1, 'Layer Prosciutto', 'Lay out prosciutto slices in overlapping layer on plastic wrap.'),
  (23, 8, 2, 'Add Duxelles and Beef', 'Spread duxelles over prosciutto, place beef on top, roll tightly.'),
  (24, 8, 3, 'Wrap in Puff Pastry', 'Encase in puff pastry, seal edges with egg wash.'),

  -- Section 9: Bake to Perfection (Recipe 3: Spaghetti Carbonara)
  (25, 9, 1, 'Chill', 'Refrigerate wrapped Wellington for 30 minutes.'),
  (26, 9, 2, 'Brush and Score', 'Brush with egg wash, score decorative pattern on top.'),
  (27, 9, 3, 'Bake', 'Bake at 425°F for 25-30 minutes until pastry is golden and internal temp reaches 125°F.'),

  -- Section 10: Make Mascarpone Cream (Recipe 4: Mom's Chocolate Cake - PRIVATE)
  (28, 10, 1, 'Beat Egg Yolks', 'Whisk 6 egg yolks with 3/4 cup sugar until thick and pale.'),
  (29, 10, 2, 'Fold Mascarpone', 'Gently fold in 16 oz mascarpone cheese until smooth.'),
  (30, 10, 3, 'Whip Cream', 'Fold in 1 cup whipped heavy cream for lightness.'),

  -- Section 11: Soak Ladyfingers (Recipe 4: Mom's Chocolate Cake - PRIVATE)
  (31, 11, 1, 'Make Coffee', 'Brew 2 cups strong espresso, add 3 tbsp coffee liqueur.'),
  (32, 11, 2, 'Dip Quickly', 'Dip ladyfingers in coffee mixture for 2 seconds per side.'),
  (33, 11, 3, 'Layer in Dish', 'Arrange soaked ladyfingers in bottom of 9x13 dish.'),

  -- Section 12: Layer and Chill (Recipe 4: Mom's Chocolate Cake - PRIVATE)
  (34, 12, 1, 'Add Cream', 'Spread half the mascarpone cream over ladyfingers.'),
  (35, 12, 2, 'Repeat Layers', 'Add another layer of soaked ladyfingers and remaining cream.'),
  (36, 12, 3, 'Dust and Chill', 'Dust with cocoa powder, refrigerate for at least 6 hours.'),

  -- Section 13: Make Tomato Base (Recipe 5: Margherita Pizza)
  (37, 13, 1, 'Sauté Vegetables', 'Cook 1 diced onion and 1 diced bell pepper in olive oil for 5 minutes.'),
  (38, 13, 2, 'Add Spices', 'Stir in cumin, paprika, and cayenne, cook for 1 minute.'),
  (39, 13, 3, 'Add Tomatoes', 'Pour in 28 oz crushed tomatoes, simmer for 15 minutes.'),

  -- Section 14: Poach Eggs (Recipe 5: Margherita Pizza)
  (40, 14, 1, 'Create Wells', 'Make 6 indentations in the tomato sauce with a spoon.'),
  (41, 14, 2, 'Add Eggs', 'Crack one egg into each well.'),
  (42, 14, 3, 'Cover and Cook', 'Cover pan and cook for 8-10 minutes until whites are set.'),

  -- Section 15: Finish and Serve (Recipe 5: Margherita Pizza)
  (43, 15, 1, 'Check Doneness', 'Ensure egg whites are set but yolks remain runny.'),
  (44, 15, 2, 'Garnish', 'Sprinkle with fresh cilantro, parsley, and crumbled feta.'),
  (45, 15, 3, 'Serve', 'Serve hot with crusty bread for dipping.'),

  -- Section 16: Mix Meatball Ingredients (Recipe 6: Nonna's Pasta Sauce - PRIVATE)
  (46, 16, 1, 'Combine Meats', 'Mix 1 lb beef, 1/2 lb pork, 1/2 lb veal with breadcrumbs, eggs, and secret spices.'),
  (47, 16, 2, 'Season and Rest', 'Add family spice blend, let mixture rest for 30 minutes.'),

  -- Section 17: Shape and Simmer (Recipe 6: Nonna's Pasta Sauce - PRIVATE)
  (48, 17, 1, 'Form and Brown', 'Roll into 2-inch balls, brown in batches in hot oil.'),
  (49, 17, 2, 'Simmer', 'Transfer to tomato sauce, simmer covered for 2 hours.'),

  -- Section 18: Prep Ingredients (Recipe 7: Thai Green Curry)
  (50, 18, 1, 'Dice Pancetta', 'Cut 8 oz pancetta into 1/4-inch cubes.'),
  (51, 18, 2, 'Mix Egg Mixture', 'Whisk 4 eggs with 1 cup grated pecorino romano and black pepper.'),
  (52, 18, 3, 'Boil Water', 'Bring large pot of salted water to boil for pasta.'),

  -- Section 19: Cook Pasta and Pancetta (Recipe 7: Thai Green Curry)
  (53, 19, 1, 'Cook Spaghetti', 'Boil 1 lb spaghetti until al dente, reserve 1 cup pasta water.'),
  (54, 19, 2, 'Crisp Pancetta', 'Cook pancetta over medium heat until crispy and golden.'),
  (55, 19, 3, 'Remove from Heat', 'Turn off heat, let pan cool slightly.'),

  -- Section 20: Combine and Toss (Recipe 7: Thai Green Curry)
  (56, 20, 1, 'Add Pasta', 'Add drained pasta to pancetta pan, toss to coat.'),
  (57, 20, 2, 'Add Egg Mixture', 'Pour egg mixture over pasta, toss quickly off heat.'),
  (58, 20, 3, 'Adjust Consistency', 'Add pasta water as needed to create creamy sauce, serve immediately.'),

  -- Section 21: Marinate Chicken (Recipe 8: Uncle's BBQ Sauce - PRIVATE)
  (59, 21, 1, 'Cut Chicken', 'Cut 2 lbs chicken breast into 1-inch cubes.'),
  (60, 21, 2, 'Make Marinade', 'Mix yogurt, ginger, garlic, garam masala, cumin, and paprika.'),
  (61, 21, 3, 'Marinate', 'Coat chicken in marinade, refrigerate for 4-24 hours.'),

  -- Section 22: Grill Chicken (Recipe 8: Uncle's BBQ Sauce - PRIVATE)
  (62, 22, 1, 'Preheat', 'Preheat broiler or grill to high heat.'),
  (63, 22, 2, 'Thread Skewers', 'Thread marinated chicken onto skewers.'),
  (64, 22, 3, 'Cook', 'Grill for 12-15 minutes, turning occasionally until charred and cooked through.'),

  -- Section 23: Make Curry Sauce (Recipe 8: Uncle's BBQ Sauce - PRIVATE)
  (65, 23, 1, 'Sauté Aromatics', 'Cook onions, ginger, garlic in butter until golden.'),
  (66, 23, 2, 'Add Spices and Tomatoes', 'Stir in spices, add crushed tomatoes and cream.'),
  (67, 23, 3, 'Simmer with Chicken', 'Add grilled chicken, simmer for 15 minutes, garnish with cilantro.'),

  -- Section 24: Mix Batter (Recipe 9: Quinoa Salad)
  (68, 24, 1, 'Combine Dry', 'Whisk 2 cups flour, 2 tbsp sugar, 2 tsp baking powder, 1/2 tsp salt.'),
  (69, 24, 2, 'Mix Wet', 'Whisk 2 cups buttermilk, 2 eggs, 1/4 cup melted butter.'),
  (70, 24, 3, 'Fold Together', 'Gently fold wet into dry until just combined, some lumps are okay.'),

  -- Section 25: Cook Pancakes (Recipe 9: Quinoa Salad)
  (71, 25, 1, 'Heat Griddle', 'Preheat griddle to 375°F, lightly grease.'),
  (72, 25, 2, 'Pour and Add Berries', 'Pour 1/4 cup batter per pancake, sprinkle with fresh blueberries.'),
  (73, 25, 3, 'Flip', 'Flip when bubbles form and edges look set, cook 2 more minutes.'),

  -- Section 26: Serve Hot (Recipe 9: Quinoa Salad)
  (74, 26, 1, 'Stack', 'Stack 3-4 pancakes per serving.'),
  (75, 26, 2, 'Top', 'Add pat of butter on top.'),
  (76, 26, 3, 'Drizzle', 'Drizzle with warm maple syrup and extra fresh blueberries.'),

  -- Section 27: Prepare Eggplant (Recipe 10: Dad's Chili Recipe - PRIVATE)
  (77, 27, 1, 'Slice', 'Cut 2 large eggplants into 1/4-inch thick rounds.'),
  (78, 27, 2, 'Salt', 'Sprinkle with salt, let sit 30 minutes to draw out moisture.'),
  (79, 27, 3, 'Grill', 'Brush with olive oil, grill or bake until golden on both sides.'),

  -- Section 28: Make Meat Sauce (Recipe 10: Dad's Chili Recipe - PRIVATE)
  (80, 28, 1, 'Brown Meat', 'Cook 1.5 lbs ground lamb or beef with onions and garlic.'),
  (81, 28, 2, 'Add Seasonings', 'Stir in cinnamon, oregano, crushed tomatoes, tomato paste.'),
  (82, 28, 3, 'Simmer', 'Simmer for 30 minutes until thickened.'),

  -- Section 29: Layer and Bake (Recipe 10: Dad's Chili Recipe - PRIVATE)
  (83, 29, 1, 'Layer Eggplant', 'Arrange half the eggplant in bottom of 9x13 dish.'),
  (84, 29, 2, 'Add Meat and Repeat', 'Spread meat sauce, top with remaining eggplant.'),
  (85, 29, 3, 'Top and Bake', 'Pour béchamel sauce over top, bake at 350°F for 45 minutes until golden.'),

  -- Section 30: Make Shortbread Crust (Recipe 11: Lasagna Bolognese)
  (86, 30, 1, 'Mix Dough', 'Combine 2 cups flour, 1/2 cup powdered sugar, 1 cup softened butter.'),
  (87, 30, 2, 'Press', 'Press evenly into bottom of 9x13 pan.'),
  (88, 30, 3, 'Bake', 'Bake at 350°F for 18-20 minutes until lightly golden.'),

  -- Section 31: Prepare Lemon Filling (Recipe 11: Lasagna Bolognese)
  (89, 31, 1, 'Whisk Eggs', 'Beat 4 eggs with 1.5 cups granulated sugar.'),
  (90, 31, 2, 'Add Lemon', 'Whisk in 1/2 cup fresh lemon juice, zest of 2 lemons, 1/4 cup flour.'),
  (91, 31, 3, 'Pour', 'Pour filling over hot crust immediately.'),

  -- Section 32: Bake and Cool (Recipe 11: Lasagna Bolognese)
  (92, 32, 1, 'Bake', 'Bake at 350°F for 20-25 minutes until filling is set.'),
  (93, 32, 2, 'Cool Completely', 'Let cool to room temperature, then refrigerate for 2 hours.'),
  (94, 32, 3, 'Cut and Serve', 'Dust with powdered sugar, cut into squares.'),

  -- Section 33: Make Cake Layers (Recipe 12: Family Curry Powder - PRIVATE)
  (95, 33, 1, 'Mix Batter', 'Combine flour, cocoa, sugar, eggs, buttermilk, and secret ingredient.'),
  (96, 33, 2, 'Bake', 'Divide between two 9-inch pans, bake at 350°F for 30-35 minutes.'),

  -- Section 34: Frost and Assemble (Recipe 12: Family Curry Powder - PRIVATE)
  (97, 34, 1, 'Make Frosting', 'Beat butter, cocoa, powdered sugar, and cream until fluffy.'),
  (98, 34, 2, 'Assemble', 'Layer cakes with frosting, frost sides and top generously.');

SELECT setval('steps_id_seq', (SELECT MAX(id) FROM steps));
