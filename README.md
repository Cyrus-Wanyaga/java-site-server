# Java Site Server

## About

This is a simple REST application that allows user's to specify a port and website files
that will be served on the provided port.  

## Motivations

As a developer that has created, and deployed numerous sites with React (CRA), it became a herculian task having to
write the static server, point it to the write path, configure SSL (if available) and then start the service which
also needs to be handled by another program like pm2 to ensure the service persists. 

To eliminate all of that hustle,
this idea was born. The backend will interface with some web application UI, where user's can easily test for a port 
before deployment, upload their zipped file and provide a name for the site then everything else will be taken care of.

## Dependencies

For those that want to continue development of this application, you will need to have:
1. Java 17+
2. Maven
3. Apache TomEE (v9+)
