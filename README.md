
# Web Application Overview

This web application is built using Jetty as the servlet container, with a Java-based backend and a simple HTML/CSS frontend. The application features user authentication and rate limiting for both login attempts and API usage.

## Accessing the Application

The application’s web interface can be accessed at:

**[http://localhost:8080/login](http://localhost:8080/login)**

### Predefined Users
The application includes a set of predefined users stored in an SQLite database. Each user has an encrypted password, and by default, the password for all users is set to:

**`password`**

## Rate Limiting
The application implements rate-limiting mechanisms on the following:
- **Login Attempts**: To prevent brute-force attacks.
- **API Usage**: Each user has a rate limit on API calls to prevent overuse.

## Docker Compose Commands

### Build docker images
```bash
docker-compose down
docker-compose build
docker-compose up
```

### Starting Services
To download required docker images and start the containers, use:
```bash
docker-compose -f docker-compose.yml up -d
```

### Stopping Services
To stop running services without removing containers, use:
```bash
docker-compose stop
```

### Stopping and Removing Containers, Networks, etc.
```bash
docker-compose down
```

### Stopping and Removing Containers with Volumes
```bash
docker-compose down --volumes
```

### Stopping and Removing Containers with Images
```bash
docker-compose down --rmi <all|local>
```

## Enabling File Logging

Follow these steps to set up file logging for the application:

1. Create the necessary logging directory:
    ```bash
    sudo mkdir -p /var/log/myapp
    ```

2. Change ownership of the logging directory:
    ```bash
    sudo chown <myappuser>:<myappuser> /var/log/myapp
    ```

3. Set appropriate permissions for the directory:
    ```bash
    sudo chmod 755 /var/log/myapp
    ```

4. Create the log file:
    ```bash
    sudo touch /var/log/myapp/application.log
    ```

5. Change ownership of the log file:
    ```bash
    sudo chown <myappuser>:<myappuser> /var/log/myapp/application.log
    ```

6. Set appropriate permissions for the log file:
    ```bash
    sudo chmod 644 /var/log/myapp/application.log
    ```