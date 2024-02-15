# Auto Show Website - RESTful Spring Web Application

<div align="center">
  <img src="public/images/logo.png" alt="Image Description">
</div>

<div align="center">
  <span>Deployed: <a href="https://danov-autoshow-656625355b99.herokuapp.com/">HERE</a></span>
</div>

## Description

Welcome to Danov's Auto Show, a comprehensive platform designed to showcase vehicles with detailed, close-to-reality 3D models and specifications. Our user-friendly interface provides seamless options to explore and inspect vehicles closely, allowing users to examine every detail. From advanced features to intricate designs, users can delve into the specifics of each vehicle, including their equipment and specifications.

Additionally, our platform offers convenient functionalities such as user authentication with options to login and register. Users can also easily add vehicles to their favorites and place orders from a vast selection of over 25,000 different models available on the site.

Experience the world of automobiles like never before with Danov's Auto Show.

## Table of Contents

- [Description](#description)
- [Built With](#BuiltWith)
- [License](#license)

## Built With
<ul dir="auto">
  <li><img src="https://camo.githubusercontent.com/a3a9d6267c299b28e81e97f2516c16895599f2dcd8b9db1a22eb2d2fb9b32b46/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f4a6176612d454434323336" alt="Java" data-canonical-src="https://img.shields.io/badge/Java-ED4236" style="max-width: 100%;"></li>
  <li><a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/e5d9b25055cdc7a6a057583523c67a5b6dc6df64c186c92c52b503667812c901/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d426f6f742d253233364242313344"><img src="https://camo.githubusercontent.com/e5d9b25055cdc7a6a057583523c67a5b6dc6df64c186c92c52b503667812c901/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d426f6f742d253233364242313344" alt="SpringBoot" data-canonical-src="https://img.shields.io/badge/Spring-Boot-%236BB13D" style="max-width: 100%;"></a></li>
  <li><a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/00984cbaea44f396be8d203bbae1efd86a401f1246acaf7c13d101d01c10629c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d446174614a50412d253233364242313344"><img src="https://camo.githubusercontent.com/00984cbaea44f396be8d203bbae1efd86a401f1246acaf7c13d101d01c10629c/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d446174614a50412d253233364242313344" alt="SpringDataJPA" data-canonical-src="https://img.shields.io/badge/Spring-DataJPA-%236BB13D" style="max-width: 100%;"></a></li>
  <li><a target="_blank" rel="noopener noreferrer nofollow" href="https://camo.githubusercontent.com/40ad96b006840ae7ccdd5f6d05e73411fc5a8dfd3cb7edf49cddd5ab60a59522/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d53656375726974792d253233443433353334"><img src="https://camo.githubusercontent.com/40ad96b006840ae7ccdd5f6d05e73411fc5a8dfd3cb7edf49cddd5ab60a59522/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f537072696e672d53656375726974792d253233443433353334" alt="SpringSecurity" data-canonical-src="https://img.shields.io/badge/Spring-Security-%23D43534" style="max-width: 100%;"></a></li>
  <li><img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/JavaScript-323330?style=for-the-badge&logo=javascript&logoColor=F7DF1E"></img></li>
  <li><img src="https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white"></img></li>
  <li><img src="https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white"></img></li>
</ul>

## Front-End

#### Home Page:
![Screenshot_22](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/f786cbf5-a383-44d6-b1f9-bb808f3272f3)
- The home page serves as the entry point to the website.
- It features a sticky header with navigation options to other pages of the site.
- Banner animation featuring sliding images of cars.
- Users can find a small preview showcasing some of the vehicles available on the site.
- Additionally, there's a "Recently Viewed" section displaying the last seen vehicles by the user.
  - Viewed cars are saved to the browser cookies if the user is not logged in; otherwise, they are saved to the database.
  - The list of viewed cars is updated every time a user views a car, regardless of their login status.
  - Also the user can add the vehicles to favorites, if they are logged in.
  - ![Screenshot_25](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/9a58b922-d60d-4fd1-b3db-86c17e9f79e3)
- The footer section includes newsletter subscription functionality.
  - Users can subscribe to the site's newsletter by entering their email address.
  - The newsletter subscription functionality validates email addresses to ensure they are valid.
  - It also checks if the email is not already subscribed to the newsletter, preventing duplicate subscriptions.
  - ![Screenshot_26](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/506d73d5-686b-4ff6-8462-80f893c01c7a)
  - ![Screenshot_27](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/b1f7869f-695e-4b7d-8f28-17cffd28f6a4)

#### Auto Show Page:
![Screenshot_23](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/b7d9ac5b-e28e-4191-841a-422564d0faac)
- The Autoshow page showcases a collection of 9 cars available for viewing.
- Unregistered users can explore the featured cars and their specifications.
- Registered users have the additional functionality of adding cars to their favorites directly from this page.
- An "Order" button is available for registered users, allowing them to place orders for vehicles.
  - The "Order" functionality filters the available models based on the selected manufacturer and year.
  - Users can specify their preferences by selecting the manufacturer and year of the desired vehicle.
  - This feature streamlines the process of finding specific models tailored to the user's preferences.
  ![Screenshot_24](https://github.com/DenisDanov/Autoshow-Website/assets/122882697/434cef67-d42b-4fab-abc5-6395654b9bc5)
