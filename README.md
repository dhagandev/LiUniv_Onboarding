# Lithium University Onboarding

## Getting Started ##
Install Java 8. We recommend installing IntelliJ to edit files, but any editor should work so long as it does not change file formats. Remember to include Java in your $PATH.

## Running the Lithium University Onboarding Program ##

### Fill in Missing Information ###
1. Open the project in IntelliJ and navigate to the ```application-example.yml``` file.
2. Copy and paste this file into your home directory; rename it ```application.yml```
3. Fill in the fields for the consumerKey, consumerSecret, accessToken, and accessSecret within ```application.yml```.

For future use: the first user listed is indexed as 0, the next is indexed as 1, and so on.

### Create the JAR File ###
4. Open a terminal shell and navigate to the LiUni_Onboarding project.
5. Run ```mvn package``` to create the jar.

### Run the Program ###
#### Hitting the GET Timeline route ####
1. Run the following command in terminal: ```java -jar target/LiUni_Onboarding-1.0-SNAPSHOT.jar server application.yml```
2. In a browser, navigate to: ```http://localhost:8080/api/1.0/twitter/timeline```

#### Hitting the GET Filtered Timeline route ####
1. Run the following command in terminal: ```java -jar target/LiUni_Onboarding-1.0-SNAPSHOT.jar server application.yml```
2. In a browser, navigate to: ```http://localhost:8080/api/1.0/twitter/tweet/filter?filterMessage=<search-term>```

#### Hitting the POST Status route ####
1. Run the following command in terminal: ```java -jar target/LiUni_Onboarding-1.0-SNAPSHOT.jar server application.yml```
2. Open Postman and create a POST route with ```http://localhost:8080/api/1.0/twitter/tweet``` as your route.
3. Under "Body", select "x-www-form-urlencoded", and add at least one parameter with the key "message" and the value being the message you want to tweet.

## Testing the Lithium University Onboarding Program ##
First, navigate to the directory the project is in within terminal.
### Running an individual set of tests ###
Run: ```mvn test -Dtest=<Java Test Class Name>```
### Running all tests ###
Run: ```mvn clean test```
### Viewing your test coverage ###
Run: ```mvn jacoco:report```.
Navigate to your project in a folder directory. Find `target/site/jacoco` and run `index.html`.

## Viewing the Lithium University Onboarding Program's Log Files ##
Navigate to ```target/log/debug.log``` to see the log messages produced.

## Adding Users ##
In the ```application.yml``` file, under the twitter tag copy the template from the ```- consumerKey:``` down to the ```accessSecret:``` and paste as many as you need users. Fill in the fields as necessary.
Currently the only way to switch between users is by indicating which user to use in the code itself.