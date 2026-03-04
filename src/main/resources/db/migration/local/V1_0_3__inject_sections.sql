INSERT INTO sections (id, recipe_id, number, title, description)
VALUES
  -- Recipe 1: Shakshuka (PUBLIC)
  (1, 1, 1, 'Caramelize Onions', 'Slowly cook onions until deep golden brown for maximum flavor.'),
  (2, 1, 2, 'Build the Soup', 'Add broth and seasonings, simmer to develop rich flavors.'),
  (3, 1, 3, 'Gratinée and Serve', 'Top with bread and cheese, broil until bubbly and golden.'),

  -- Recipe 2: Grandma's Secret Meatballs (PRIVATE)
  (4, 2, 1, 'Make Dressing', 'Prepare creamy caesar dressing with anchovies and garlic.'),
  (5, 2, 2, 'Prepare Croutons', 'Toast bread cubes with olive oil and garlic until crispy.'),
  (6, 2, 3, 'Assemble Salad', 'Toss romaine with dressing, top with parmesan and croutons.'),

  -- Recipe 3: Spaghetti Carbonara (PUBLIC)
  (7, 3, 1, 'Prepare Beef and Duxelles', 'Sear beef and make mushroom paste for wrapping.'),
  (8, 3, 2, 'Wrap in Pastry', 'Layer prosciutto, duxelles, and wrap beef in puff pastry.'),
  (9, 3, 3, 'Bake to Perfection', 'Bake until pastry is golden and beef reaches desired doneness.'),

  -- Recipe 4: Mom's Chocolate Cake (PRIVATE)
  (10, 4, 1, 'Make Mascarpone Cream', 'Whip mascarpone with eggs and sugar until light and fluffy.'),
  (11, 4, 2, 'Soak Ladyfingers', 'Dip ladyfingers in espresso and layer in dish.'),
  (12, 4, 3, 'Layer and Chill', 'Alternate cream and cookies, dust with cocoa, refrigerate.'),

  -- Recipe 5: Margherita Pizza (PUBLIC)
  (13, 5, 1, 'Make Tomato Base', 'Cook onions, peppers, and spices in tomato sauce.'),
  (14, 5, 2, 'Poach Eggs', 'Create wells in sauce and gently crack eggs into them.'),
  (15, 5, 3, 'Finish and Serve', 'Cook until eggs are set, garnish with herbs and feta.'),

  -- Recipe 6: Nonna's Pasta Sauce (PRIVATE)
  (16, 6, 1, 'Mix Meatball Ingredients', 'Combine meats with secret spice blend and binders.'),
  (17, 6, 2, 'Shape and Simmer', 'Form into balls, brown, and simmer in sauce until tender.'),

  -- Recipe 7: Thai Green Curry (PUBLIC)
  (18, 7, 1, 'Prep Ingredients', 'Prepare pancetta, eggs, cheese, and bring water to boil.'),
  (19, 7, 2, 'Cook Pasta and Pancetta', 'Boil spaghetti and crisp pancetta in pan.'),
  (20, 7, 3, 'Combine and Toss', 'Mix hot pasta with egg mixture off heat for creamy sauce.'),

  -- Recipe 8: Uncle's BBQ Sauce (PRIVATE)
  (21, 8, 1, 'Marinate Chicken', 'Coat chicken in yogurt and spices, refrigerate.'),
  (22, 8, 2, 'Grill Chicken', 'Cook marinated chicken until charred and cooked through.'),
  (23, 8, 3, 'Make Curry Sauce', 'Simmer tomato cream sauce with spices, add chicken.'),

  -- Recipe 9: Quinoa Salad (PUBLIC)
  (24, 9, 1, 'Mix Batter', 'Combine wet and dry ingredients until just mixed.'),
  (25, 9, 2, 'Cook Pancakes', 'Pour batter on griddle, add blueberries, flip when bubbly.'),
  (26, 9, 3, 'Serve Hot', 'Stack pancakes, top with butter and maple syrup.'),

  -- Recipe 10: Dad's Chili Recipe (PRIVATE)
  (27, 10, 1, 'Prepare Eggplant', 'Slice, salt, and grill eggplant until golden.'),
  (28, 10, 2, 'Make Meat Sauce', 'Cook spiced lamb or beef sauce with tomatoes.'),
  (29, 10, 3, 'Layer and Bake', 'Layer eggplant, meat, béchamel, bake until golden.'),

  -- Recipe 11: Lasagna Bolognese (PUBLIC)
  (30, 11, 1, 'Make Shortbread Crust', 'Mix butter, flour, and sugar, press into pan and bake.'),
  (31, 11, 2, 'Prepare Lemon Filling', 'Whisk eggs, sugar, lemon juice and zest, pour over crust.'),
  (32, 11, 3, 'Bake and Cool', 'Bake until set, cool completely, dust with powdered sugar.'),

  -- Recipe 12: Family Curry Powder (PRIVATE)
  (33, 12, 1, 'Make Cake Layers', 'Mix batter with secret ingredient, bake in two pans.'),
  (34, 12, 2, 'Frost and Assemble', 'Make fudge frosting, layer and frost the cooled cakes.');

SELECT setval('sections_id_seq', (SELECT MAX(id) FROM sections));
