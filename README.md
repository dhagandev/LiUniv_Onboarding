# Lithium University Onboarding

## Getting Started ##
TODO: Add set up instructions.
Include: Reminder to set Java in $PATH

## Running the Lithium University Onboarding Program ##

### Fill in Missing Information ###
1. Open the project in IntelliJ and navigate to the ```hardcoded_keys.xml``` file.
2. Fill in the fields for the consumerKey, consumerSecret, accessToken, and accessSecret.

### Create the JAR File ###
3. Open a terminal shell and navigate to the LiUni_Onboarding project.
4. Run ```javac -cp twitter4j-core-4.0.4.jar src/main/java/LiUni/*.java``` to compile the project.
5. Run ```jar cfm out/artifacts/LiUni_Onboarding_jar/LiUni_Onboarding.jar src/main/java/META-INF/MANIFEST.MF src/main/java/LiUniInputRequests.class```

### Run the JAR File ###
6. Navigate to ```LiUni_Onboarding.jar```'s location in a terminal shell window.
?. Run the file using ```java -jar LiUni_Onboarding.jar```. Follow the prompts.


TODO: Test this README on a clean machine.