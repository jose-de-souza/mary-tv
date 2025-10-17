-- V4__Insert_100_test_records.sql

-- Insert 20 unique categories (ON CONFLICT DO NOTHING to avoid duplicates from V2)
INSERT INTO categories (name)
VALUES ('Daily Mass Reflections'),
       ('Evening Vespers'),
       ('Angelus Prayer'),
       ('Divine Mercy Hour'),
       ('Liturgy Readings'),
       ('Noon Meditation'),
       ('Night Vigil'),
       ('Adoration Live'),
       ('Confession Guide'),
       ('Scripture Study'),
       ('Saints Lives'),
       ('Pilgrimage Journeys'),
       ('Medjugorje Talks'),
       ('Apparition Witnesses'),
       ('Miracle Accounts'),
       ('Testimony Shares'),
       ('Youth Talks'),
       ('Family Prayers'),
       ('Vocation Discernment'),
       ('Ordination Ceremonies')
ON CONFLICT (name) DO NOTHING;

-- Insert 20 unique events (no UNIQUE constraint)
INSERT INTO events (name)
VALUES ('Morning Mass'),
       ('Afternoon Rosary'),
       ('Sunset Prayer'),
       ('Midnight Adoration'),
       ('Dawn Liturgy'),
       ('Vespers Service'),
       ('Lauds Chant'),
       ('Compline Reflection'),
       ('Terre Prayer'),
       ('Nocturne Vigil'),
       ('Baptism Rite'),
       ('Wedding Ceremony'),
       ('Funeral Mass'),
       ('First Communion'),
       ('Confirmation Service'),
       ('Ordination Rite'),
       ('Pilgrimage Mass'),
       ('Retreat Session'),
       ('Youth Gathering'),
       ('Family Retreat');

-- Insert 50 items (10 series headers, 20 standalone, 20 episodes - 2 per header)
-- Series Headers (10, content_type='SERIES_HEADER')
INSERT INTO items (title, description, icon_url, video_url, item_date, is_new, is_headline, content_type, category_id,
                   event_id)
VALUES ('Mass Reflections Series', 'Series on daily mass', '/icons/mass-series.png',
        'https://marytv.tv/mass-reflections', CURRENT_TIMESTAMP - INTERVAL '1 week', true, false, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Daily Mass Reflections' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Morning Mass' LIMIT 1)),
       ('Vespers Series', 'Evening prayer series', '/icons/vespers.png', 'https://marytv.tv/vespers-series',
        CURRENT_TIMESTAMP - INTERVAL '2 weeks', false, true, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Evening Vespers' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Vespers Service' LIMIT 1)),
       ('Angelus Series', 'Angelus prayer collection', '/icons/angelus.png', 'https://marytv.tv/angelus-series',
        CURRENT_TIMESTAMP - INTERVAL '3 weeks', true, false, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Angelus Prayer' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Noon Meditation' LIMIT 1)),
       ('Mercy Hour Series', 'Divine Mercy hours', '/icons/mercy.png', 'https://marytv.tv/mercy-series',
        CURRENT_TIMESTAMP - INTERVAL '4 weeks', false, false, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Divine Mercy Hour' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Afternoon Rosary' LIMIT 1)),
       ('Liturgy Series', 'Liturgy readings', '/icons/liturgy.png', 'https://marytv.tv/liturgy-series',
        CURRENT_TIMESTAMP - INTERVAL '5 weeks', true, true, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Liturgy Readings' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Dawn Liturgy' LIMIT 1)),
       ('Meditation Series', 'Noon meditations', '/icons/meditation.png', 'https://marytv.tv/meditation-series',
        CURRENT_TIMESTAMP - INTERVAL '6 weeks', false, false, 'SERIES_HEADER',
        (SELECT id FROM categories WHERE name = 'Noon Meditation' LIMIT 1),
        (SELECT id FROM events WHERE name = 'Sunset Prayer' LIMIT 1))
