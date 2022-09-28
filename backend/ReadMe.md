# Local setup for expense tracker

1. software installation
2. database setup
3. backend setup
4. ui setup

----


** 1. Software installation **
 - Install postgress database
 - install JDK ( version 8 or higher)
 - install Node (version 14)
 - install apache maven (just get the latest version)
 - install git (just get the latest version)


** 2. Database setup **
 - create the tables & add the referential data
 - connect to the database using the sql client of choice and execute the statements from the below file one by one
 - sql file location: `finance-manager/src/main/resources/sql/main.sql` in the backend repository (refer backend setup for repo link)

** 3.Backend setup **
 - checkout the back-end source code from github repository by executing `git clone https://github.com/balaji142857/cloud-projects.git`
 - navigate to finance-manager directory (will be inside the cloned directory)
 - execute `maven install` (this is bound to take some time on first run)
 - execute `mvn spring-boot:run`
 - test the backend app status by opening this url `http://localhost:8980/actuator`

** 4. UI setup **
- checkout the UI source code from github repository by executing `git clone https://github.com/balaji142857/finance-manager-web.git`
- execute `npm install` (this again wil take time)
- execute `npm start`
- once the above command completes, open this url: `http://localhost:4200`


  