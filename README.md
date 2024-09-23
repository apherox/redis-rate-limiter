# Application info

<h3>Application has a web interface which can be accessed on:
`http://localhost:8080/login`
</h3>

# Some important docker-compose commands

## Stop services only
`docker-compose stop`

## Stop and remove containers, networks...
`docker-compose down`

## Down and remove volumes
`docker-compose down --volumes`

## Down and remove images
`docker-compose down --rmi <all|local>`


# Enable File logging

```sudo mkdir -p /var/log/myapp```

`sudo chown <myappuser>:<myappuser> /var/log/myapp`

`sudo chmod 755 /var/log/myapp`

`sudo touch /var/log/myapp/application.log`

`sudo chown <myappuser>:<myappuser> /var/log/myapp/application.log`

`sudo chmod 644 /var/log/myapp/application.log`
