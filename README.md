# backend

#Installation
 1. Install Java 12
 2. Instal Mysql
 3. In your IDE, you'll need the following plugins:
  1. Install Lombok
  
#Connection to DB
 In order to connect to MYSQL database perform the following:
  1. Start Mysql Workbench
  2. Connect with client to localhost:3306
  3. If you don't have the schema execute the following query: create schema `logistics-company`;
  
#Creating Tables in DB:
 Hibernate will do all the work for you. Just make sure you application.yaml configurations are correct. 
 If you don't have the tables, the settings should be: hibernate.ddl-auto: create. All option are: validate | update | create | create-drop.
 My advice is to use "update" most of the time and "create" only when needed.
