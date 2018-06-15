# Lithium University Onboarding

## Getting Started ##
Install Java 8. We recommend installing IntelliJ to edit files, but any editor should work so long as it does not change file formats. Remember to include Java in your $PATH.

## Running the Lithium University Onboarding Program ##

### Fill in Missing Information ###
1. Open the project in IntelliJ and navigate to the ```hardcoded_keys_template.xml``` file.
2. Copy and paste this file into your home directory; rename it ```hardcoded_keys.xml```
3. Fill in the fields for the consumerKey, consumerSecret, accessToken, and accessSecret within ```hardcoded_keys.xml```.

### Create the JAR File ###
4. Open a terminal shell and navigate to the LiUni_Onboarding project.
5. Run ```mvn package``` to create the jar.
6. Move your ```hardcoded_keys.xml``` to ```target```.

### Run the JAR File ###
7. Navigate to ```LiUni_Onboarding-1.0-SNAPSHOT-jar-with-dependencies.jar```'s location in a terminal shell window. It is likely in the ```target``` folder.
8. Run the file using ```java -jar LiUni_Onboarding-1.0-SNAPSHOT-jar-with-dependencies.jar```. Follow the prompts.

## Exit Codes ##
If the program exits prematurely, a status code will be given. The following is a table of what the possible codes are.
Exit code -1: Twitter Exception
Exit code -2: Key Handler