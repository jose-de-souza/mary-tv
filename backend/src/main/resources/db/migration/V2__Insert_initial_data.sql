-- Sample categories
INSERT INTO categories (name) VALUES
                                  ('Live Streams'),
                                  ('Prayers'),
                                  ('Archives');

-- Sample events
INSERT INTO events (name) VALUES
                              ('Daily Rosary'),
                              ('Weekly Mass');

-- Sample items
INSERT INTO items (title, description, icon_url, video_url, item_date, is_new, is_headline, category_id, event_id) VALUES
                                                                                                                       ('First Live Stream', 'Test live prayer', '/icons/live.png', 'https://example.com/video1.mp4', CURRENT_TIMESTAMP, true, true, 1, 1),
                                                                                                                       ('Sample Prayer', 'Recorded rosary', '/icons/prayer.png', 'https://example.com/video2.mp4', CURRENT_TIMESTAMP, false, false, 2, 2);