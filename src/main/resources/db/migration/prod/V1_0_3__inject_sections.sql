INSERT INTO sections (id, recipe_id, number, title, description)
VALUES
  -- Recipe 1: Shakshuka (PUBLIC)
  (1, 1, 1, 'Caramelize Onions', 'This is the foundation of authentic French onion soup and requires patience. The slow caramelization process transforms ordinary yellow onions into deeply sweet, mahogany-colored ribbons that provide the rich, complex flavor base. You''ll want to slice your onions thinly and uniformly to ensure even cooking. The key is maintaining a medium-low temperature throughout - if the heat is too high, the onions will burn rather than caramelize. Stir occasionally, allowing the onions to develop color between stirs. This process typically takes 45-60 minutes, but the result is worth every minute. The natural sugars in the onions will concentrate and caramelize, creating that signature sweet-savory flavor that makes this soup legendary.'),
  (2, 1, 2, 'Build the Soup', 'Once your onions have reached that perfect deep golden-brown color, it''s time to transform them into soup. Deglaze the pan with wine to capture all those flavorful brown bits stuck to the bottom - this is pure flavor gold. Then add your beef broth, which should be high-quality and preferably homemade if possible. Fresh thyme sprigs and bay leaves add aromatic depth, while the salt and pepper should be adjusted carefully to taste. Allow the soup to simmer gently for at least 30 minutes, giving all the flavors time to marry together. The broth will reduce slightly and intensify in flavor. Some traditional recipes even simmer for up to an hour for maximum depth.'),
  (3, 1, 3, 'Gratinée and Serve', 'The gratinée topping is what elevates French onion soup from good to extraordinary. Start with a quality baguette sliced into rounds about 1 inch thick and toast them until completely dry and golden - this prevents them from getting soggy in the soup. Ladle the piping hot soup into individual oven-safe crocks, leaving about an inch from the top. Float the toasted bread on the surface and cover generously with freshly grated Gruyère cheese, making sure the cheese extends to the edges of the crock. Place under a preheated broiler until the cheese is melted, bubbling vigorously, and develops those beautiful golden-brown spots. The cheese should form a stretchy, savory blanket over the soup. Serve immediately while the cheese is still molten and the soup is steaming hot.'),

  -- Recipe 2: Grandma's Secret Meatballs (PRIVATE)
  (4, 2, 1, 'Make Dressing', 'The key to an authentic Caesar dressing is using quality ingredients and the proper emulsification technique. Start with room temperature egg yolks, which will help create a silky, stable emulsion. The anchovies are non-negotiable - they provide that signature umami depth that makes Caesar dressing so addictive. Mince them into a paste along with fresh garlic cloves. The garlic should be finely minced or even grated on a microplane for the best distribution of flavor. Fresh lemon juice adds brightness and acidity to cut through the richness. As you slowly drizzle in the olive oil while whisking constantly, you''ll watch the mixture transform from thin and separated to thick and creamy. This emulsification is crucial - adding the oil too quickly will cause the dressing to break. The final result should be thick enough to coat the back of a spoon and have a pale, creamy color.'),
  (5, 2, 2, 'Prepare Croutons', 'Homemade croutons are infinitely superior to store-bought and surprisingly easy to make. The best croutons start with slightly stale bread - day-old crusty bread is ideal because it absorbs the oil and seasonings without getting soggy. Cut the bread into uniform cubes, about 1 inch each, so they cook evenly. Toss them generously with good quality olive oil - don''t be shy here, as the oil carries flavor and helps achieve that golden, crispy exterior. Freshly minced garlic adds aromatic punch, while salt and freshly ground black pepper season throughout. Spread the seasoned bread cubes in a single layer on a baking sheet to ensure even toasting. Bake at 375°F, stirring halfway through, until they''re golden brown and completely crispy throughout, about 12-15 minutes. They should be crunchy all the way through, not just on the outside. Let them cool completely - they''ll crisp up even more as they cool.'),
  (6, 2, 3, 'Assemble Salad', 'Assembly is the final crucial step that brings all components together. Start with completely dry romaine lettuce - any water will dilute your carefully crafted dressing and make the salad soggy. The outer leaves can be torn into bite-sized pieces, while the tender inner leaves can be left larger for texture variation. Add about two-thirds of your dressing initially and toss vigorously, using your hands or tongs to ensure every leaf is coated. The leaves should glisten with dressing but not be drowning in it - you can always add more, but you can''t remove excess. Taste a leaf and adjust seasoning if needed. Add most of your croutons and toss again, reserving some for garnish. Finally, using a vegetable peeler, shave generous curls of Parmigiano-Reggiano directly over the salad. The fresh-shaved cheese adds both flavor and visual appeal. Top with the reserved croutons, an extra shower of Parmesan, and freshly cracked black pepper. Serve immediately while the croutons are still crispy.'),

  -- Recipe 3: Spaghetti Carbonara (PUBLIC)
  (7, 3, 1, 'Prepare Beef and Duxelles', 'Beef Wellington is an impressive centerpiece that requires careful preparation of its key components. Start with a high-quality beef tenderloin, trimmed of any silver skin and tied if necessary to maintain an even shape. Season it generously with salt and freshly ground black pepper. Heat a heavy skillet until it''s smoking hot - this high heat is essential for developing a proper crust. Sear the beef quickly on all sides, about 1-2 minutes per side, until it develops a deep golden-brown crust. This not only adds flavor but also helps seal in the juices. Remove immediately and let it cool completely. Meanwhile, prepare the duxelles, a finely minced mushroom mixture that''s been called the soul of Beef Wellington. Use a mix of mushrooms for complexity - cremini, shiitake, and porcini work beautifully. Mince them very finely, almost to a paste, and cook slowly with shallots in butter until all the moisture has evaporated. This concentration is crucial - any residual moisture will make your pastry soggy. Season with fresh thyme, salt, and pepper, then cool completely before assembly.'),
  (8, 3, 2, 'Wrap in Pastry', 'The wrapping technique is what transforms simple components into the legendary Beef Wellington. Start by laying out overlapping slices of thinly sliced prosciutto on a large sheet of plastic wrap, creating a rectangle slightly larger than your beef. The prosciutto serves multiple purposes - it adds flavor, helps hold the duxelles in place, and creates a moisture barrier to protect the pastry. Spread your cooled duxelles in an even layer over the prosciutto, leaving a small border. Place the cooled, seared beef in the center and use the plastic wrap to tightly roll the prosciutto and duxelles around the beef, creating a tight cylinder. Twist the ends of the plastic wrap to secure and refrigerate for at least 30 minutes - this helps everything set and makes wrapping in pastry easier. Roll out your puff pastry into a rectangle large enough to fully encase the beef bundle. Unwrap the beef from the plastic and place it seam-side down in the center of the pastry. Brush the edges with egg wash, then carefully wrap the pastry around the beef, pressing the seams to seal. Trim any excess pastry and crimp the edges.'),
  (9, 3, 3, 'Bake to Perfection', 'The final baking stage is where precision matters most - you want a golden, crispy pastry while achieving the perfect internal temperature for the beef. After chilling your wrapped Wellington for 30 minutes, brush the entire surface with egg wash for that beautiful golden sheen. Using a sharp knife, score decorative patterns on top - traditional diagonal lines or a lattice pattern work well, but don''t cut too deep or you''ll pierce the pastry. Make a small hole in the top to allow steam to escape during baking. Bake in a preheated 425°F oven for 25-30 minutes, watching carefully. The pastry should turn a deep golden brown and sound hollow when tapped. Use an instant-read thermometer inserted into the center - aim for 120-125°F for medium-rare. Remember, the beef will continue to cook as it rests, so slightly underdone is better than overdone. Let the Wellington rest for 10-15 minutes before slicing - this allows the juices to redistribute and makes for cleaner slices. Cut with a sharp serrated knife into thick slices, revealing the beautiful layers of golden pastry, prosciutto, duxelles, and perfectly pink beef.'),

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
