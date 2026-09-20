CREATE TABLE IF NOT EXISTS users(
 id BIGSERIAL PRIMARY KEY, full_name VARCHAR(150) NOT NULL,
 email VARCHAR(180) UNIQUE NOT NULL, password_hash VARCHAR(255) NOT NULL,
 phone VARCHAR(30), role VARCHAR(20) NOT NULL DEFAULT 'CITIZEN',
 is_active BOOLEAN NOT NULL DEFAULT TRUE,
 created_at TIMESTAMP NOT NULL DEFAULT now(), updated_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS categories(
 id BIGSERIAL PRIMARY KEY, name VARCHAR(100) UNIQUE NOT NULL,
 description TEXT, created_at TIMESTAMP DEFAULT now()
);
CREATE TABLE IF NOT EXISTS complaints(
 id BIGSERIAL PRIMARY KEY, tracking_number VARCHAR(50) UNIQUE NOT NULL,
 citizen_id BIGINT NOT NULL REFERENCES users(id), category_id BIGINT NOT NULL REFERENCES categories(id),
 title VARCHAR(160) NOT NULL, description TEXT NOT NULL,
 priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
 status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED',
 location VARCHAR(250), latitude DOUBLE PRECISION, longitude DOUBLE PRECISION,
 duplicate_score NUMERIC(6,5), duplicate_of BIGINT REFERENCES complaints(id),
 assigned_to BIGINT REFERENCES users(id),
 created_at TIMESTAMP NOT NULL DEFAULT now(), updated_at TIMESTAMP NOT NULL DEFAULT now(),
 resolved_at TIMESTAMP
);
CREATE TABLE IF NOT EXISTS notifications(
 id BIGSERIAL PRIMARY KEY, user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
 complaint_id BIGINT REFERENCES complaints(id) ON DELETE SET NULL,
 message VARCHAR(500) NOT NULL, is_read BOOLEAN NOT NULL DEFAULT FALSE,
 created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS complaint_feedback(
 id BIGSERIAL PRIMARY KEY, complaint_id BIGINT UNIQUE NOT NULL REFERENCES complaints(id) ON DELETE CASCADE,
 citizen_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
 rating SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5), comment VARCHAR(1000), created_at TIMESTAMP NOT NULL DEFAULT now()
);
CREATE TABLE IF NOT EXISTS complaint_history(
 id BIGSERIAL PRIMARY KEY, complaint_id BIGINT NOT NULL REFERENCES complaints(id) ON DELETE CASCADE,
 changed_by BIGINT REFERENCES users(id), old_status VARCHAR(30),
 new_status VARCHAR(30) NOT NULL, comment TEXT, created_at TIMESTAMP DEFAULT now()
);
CREATE TABLE IF NOT EXISTS attachments(
 id BIGSERIAL PRIMARY KEY, complaint_id BIGINT NOT NULL REFERENCES complaints(id) ON DELETE CASCADE,
 original_filename VARCHAR(255), stored_filename VARCHAR(255) NOT NULL,
 file_path VARCHAR(500) NOT NULL, mime_type VARCHAR(100), file_size BIGINT,
 created_at TIMESTAMP DEFAULT now()
);
CREATE INDEX IF NOT EXISTS idx_citizen ON complaints(citizen_id);
CREATE INDEX IF NOT EXISTS idx_status ON complaints(status);
CREATE INDEX IF NOT EXISTS idx_category ON complaints(category_id);
CREATE INDEX IF NOT EXISTS idx_created ON complaints(created_at);
CREATE INDEX IF NOT EXISTS idx_notifications_user ON notifications(user_id, is_read, created_at);

INSERT INTO categories(name,description) VALUES
('Roads','Potholes, damaged roads and signage'),
('Street Lights','Broken public lighting'),
('Water Supply','Water supply and pipeline issues'),
('Garbage','Waste collection and sanitation'),
('Drainage','Drainage and flooding'),
('Electricity','Public electrical infrastructure'),
('Public Safety','Civic safety issues'),
('Transportation','Public transport issues'),
('Education','Public education facilities and services'),
('Social / Community','Community facilities and social services'),
('Footpaths / Sidewalks','Damaged or inaccessible footpaths and sidewalks'),
('Traffic Signals','Traffic lights and road signal issues'),
('Public Transport','Bus stops, shelters and public transit issues'),
('Parking','Public parking and parking enforcement issues'),
('Public Toilets','Public restroom maintenance and access'),
('Pollution','Air, water or land pollution'),
('Trees / Green Spaces','Parks, trees and green-space maintenance'),
('Noise Pollution','Excessive public noise concerns'),
('Accessibility','Accessibility barriers in public spaces'),
('Waste Management','Waste processing and disposal concerns'),
('Damaged Infrastructure','Damaged public assets and infrastructure'),
('Bridges','Bridge damage, maintenance and safety issues'),
('Bus Stops','Bus-stop shelter, access and maintenance issues'),
('Traffic','Traffic flow and road-use concerns'),
('Other','Other grievances')
ON CONFLICT(name) DO NOTHING;
ALTER TABLE users ADD COLUMN IF NOT EXISTS language VARCHAR(10) NOT NULL DEFAULT 'en';
ALTER TABLE users ADD COLUMN IF NOT EXISTS notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE complaints ADD COLUMN IF NOT EXISTS latitude DOUBLE PRECISION;
ALTER TABLE complaints ADD COLUMN IF NOT EXISTS longitude DOUBLE PRECISION;
ALTER TABLE complaints ADD COLUMN IF NOT EXISTS assigned_at TIMESTAMP;
ALTER TABLE users ADD COLUMN IF NOT EXISTS authority_id VARCHAR(40);
ALTER TABLE users ADD COLUMN IF NOT EXISTS department VARCHAR(120);
ALTER TABLE users ADD COLUMN IF NOT EXISTS designation VARCHAR(120);
ALTER TABLE users ADD COLUMN IF NOT EXISTS assigned_area VARCHAR(120);
CREATE UNIQUE INDEX IF NOT EXISTS idx_authority_id ON users(authority_id) WHERE authority_id IS NOT NULL;
