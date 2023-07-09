# EmpowHer
EmpowHer is an innovative mobile application designed to provide a quick and reliable response system for women in distress. This app serves as a powerful tool in combating crimes against women by allowing them to send SOS signals and instantly report incidents to the local police authorities. Leveraging the widespread availability of smartphones and advanced communication technologies, this app aims to empower women to feel safer and enhance their overall personal security. The app's core functionality revolves around the SOS feature, which allows women to send distress signals with their location information to the nearest police station. Upon activation, the app triggers an alert, notifying the police authorities of the user's exact location and providing them with crucial details necessary for immediate response. The app establishes a direct line of communication between the user and the police, ensuring that help is dispatched efficiently and effectively.

# Register Page
This is our Register Page; in this, users can enter their name, mail, phone number, password and their designation, civilian or police. While registering for our app, the user's system id is also registered along with the user's details to check whether the registered user has a unique and validated id or not. Before registering, your system's ID and database registered id is also checked to maintain the authenticity of the user for our app. Why we used this method? To check on the pranksters who may give false information to the police. And after this, all checks of the user's details are registered to the Firebase's real-time database.

![WhatsApp Image 2023-07-02 at 01 17 43](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/4a020e22-1a17-4bd4-ad64-ce8e11a06e69)

# Login Page
This is our Login page; in this, users enter their registered name and password, and the database is parsed according to user ID, i.e. System Id and their name and password are checked, and according to that, users are logged in to the civilian page or police page.

![WhatsApp Image 2023-07-02 at 01 34 05](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/6db2a7a7-ca20-4cad-9c8c-c299c3e8f475)

# Civil Page
This is our Civilian Page for triggering the SOS. In this, users can select their problems, and while selecting others, anyone can write their own problems in the given edit text and simply send sos using the triggering button.
While triggering latitude and longitude of the person's mobile will be sent to the database along with its address. If somehow this method is failed, the sms with the address will be sent to the police by clicking on the same SOS button. 

![WhatsApp Image 2023-07-02 at 02 50 54](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/d0098567-7c99-4945-b432-f5bebc3c105c)

# Emergency Response System
This is our Emergency Response System page for tracking SOS locations. By clicking on the NERS button, you will be redirected to google maps, where the SOS location has been triggered and will be shown as the marker. Here 
when you click the photo of the police, it can also take your photo and will be sent to the google cloud fire store. 

![WhatsApp Image 2023-07-02 at 03 10 15](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/77814ef0-cba5-421c-9339-80d2b999efc9)

The GOOGLE SOS will look like this.

![WhatsApp Image 2023-07-02 at 03 12 28](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/b8047034-be21-4d98-9bd0-445ffa0cecde)

# Geofencing database
This is our Registered user's database.

![image](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/c3ae924d-d37f-401a-acf9-9ca2ee0fab16)

This is our SOS database table.

![image](https://github.com/siddhantpriyadarshi18581/Security_App/assets/77992570/a66c1d43-ced4-462e-9950-fba1700096af)

# <--------------------------THE END--------------------------->
