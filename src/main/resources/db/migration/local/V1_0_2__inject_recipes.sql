INSERT INTO recipes (id, name, description, access_level, owner_id, last_updated_at)
VALUES
  -- User 1 (admin-user): 1 PUBLIC + 1 PRIVATE
  (1,  'Shakshuka', 'Middle Eastern dish of eggs poached in spiced tomato and pepper sauce.', 'PUBLIC', 1, NOW()),
  (2,  'Grandma''s Secret Meatballs', 'Family recipe passed down through generations with a special ingredient blend.', 'PRIVATE', 1, NOW()),

  -- User 2 (regular-user-1): 1 PUBLIC + 1 PRIVATE
  (3,  'Spaghetti Carbonara', 'Traditional Italian pasta with eggs, pecorino, pancetta, and black pepper.', 'PUBLIC', 2, NOW()),
  (4, 'Mom''s Chocolate Cake', 'Ultra-moist chocolate layer cake with fudge frosting.', 'PRIVATE', 2, NOW()),

  -- User 3 (regular-user-2): 1 PUBLIC + 1 PRIVATE
  (5, 'Margherita Pizza', 'Classic Neapolitan pizza with tomato sauce, mozzarella, and fresh basil.', 'PUBLIC', 3, NOW()),
  (6, 'Nonna''s Pasta Sauce', 'Secret family marinara recipe simmered for hours.', 'PRIVATE', 3, NOW()),

  -- User 4 (regular-user-3): 1 PUBLIC + 1 PRIVATE
  (7, 'Thai Green Curry', 'Aromatic coconut curry with green chilies, basil, and vegetables.', 'PUBLIC', 4, NOW()),
  (8, 'Uncle''s BBQ Sauce', 'Award-winning barbecue sauce recipe guarded for years.', 'PRIVATE', 4, NOW()),

  -- User 5 (regular-user-4): 1 PUBLIC + 1 PRIVATE
  (9, 'Quinoa Salad', 'Mediterranean quinoa with tomatoes, cucumber, feta, and lemon dressing.', 'PUBLIC', 5, NOW()),
  (10, 'Dad''s Chili Recipe', 'Competition-winning chili with undisclosed ingredient list.', 'PRIVATE', 5, NOW()),

  -- User 6 (disabled-user-1): 1 PUBLIC + 1 PRIVATE
  (11, 'Lasagna Bolognese', 'Layered pasta with rich meat sauce and creamy béchamel.', 'PUBLIC', 6, NOW()),
  (12, 'Family Curry Powder', 'Custom spice blend passed down through three generations.', 'PRIVATE', 6, NOW())
;

SELECT setval('recipes_id_seq', (SELECT MAX(id) FROM recipes));
