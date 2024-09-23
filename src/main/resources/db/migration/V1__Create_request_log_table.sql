CREATE TABLE request_log (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    request_method TEXT NOT NULL,
    request_uri TEXT NOT NULL,
    remote_address TEXT NOT NULL,
    response_code INTEGER NOT NULL,
    user_agent TEXT NOT NULL,
    current_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
