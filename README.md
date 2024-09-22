# Docker Repository Found here - https://hub.docker.com/r/dmurphyerskine/peeppeablog


# Overview
PeepPeaStar is a Spring Boot application designed for managing blog content, user authentication, and character profiles. The application has a playful theme centered around "Peepville" characters, offering various features for users to explore blogs, characters, and other related content.


# Key Features
- **Blog Management**: Create, read, and list blogs with support for comments and user interactions.
- **User Authentication**: Registration, login, and authentication using Spring Security.
- **Character Profiles**: Display detailed profiles of "Peepville" characters.
- **Responsive UI**: Front-end designed with HTML, CSS, and Thymeleaf templates for a responsive user experience.
- **Database Integration**: Stores user, blog, comment, and message data in a PostgreSQL database.


# Project Structure
- **Main Application**: `PeepPeaStarApplication.java` and `ServletInitializer.java`
- **Beans**: `Blog.java`, `User.java`, `Comment.java`, `Message.java`
- **Configuration**: `AppConfig.java`, `SecurityConfig.java`
- **Controllers**: `BlogController.java`, `CommonController.java`, `HomeController.java`, `MessageController.java`, `UserController.java`
- **DAOs**: `BlogRepository.java`, `CommentRepository.java`, `MessageRepository.java`, `UserRepository.java`
- **DTOs**: `UserLoginDto.java`, `UserMessageDto.java`, `UserRegistrationDto.java`
- **Service**: `PeepUserDetailsService.java`
- **Resources**:
  - **Static Files**: CSS, images, fonts, JavaScript
  - **Templates**: HTML templates for various pages (`index.html`, `blog-list.html`, `register-user.html`, etc.)
- **Tests**: `PeepPeaStarApplicationTests.java`, `UserControllerTest.java`


# Beans
- **Blog**: Represents blog entries with fields for title, content, user, etc.
- **User**: Represents users with fields for name, email, password, etc.
- **Comment**: Represents comments on blog posts.
- **Message**: Represents messages exchanged between users.

# Endpoints
- `/aboutPeepPea` (GET): About page for Peep Pea.
- `/blog/{id}` (GET): Detailed view of a specific blog post.
- `/getAllBlogs` (GET): List all blogs.
- `/getCharacter/{charname}` (GET): View character profile by name.
- `/home` (GET): Home page.
- `/newUser` (GET): Registration page.
- `/peepUser` (GET): Redirect to user-specific page.
- `/saveUser` (POST): Handle user registration.
- `/search` (POST): Search functionality.


# Important Files
- **Configuration**: `SecurityConfig.java` - Configures Spring Security settings, including permitted URLs and authentication requirements.
- **Controller Logic**:
  - `UserController.java`: Manages user registration and authentication.
  - `BlogController.java`: Manages blog creation and listing.
  - `HomeController.java`: Handles home page requests.
- **Database Files**: Contains SQL files for initializing the database tables for blogs, comments, messages, and users.


# SQL Files
Located in the `psql_scripts` directory, these files are used to initialize the PostgreSQL database with the necessary tables:
- `create_blog_table.sql`: Sets up the `peeppea_blog` table for storing blog posts.
- `create_comments_table.sql`: Sets up the `peeppea_comments` table for storing comments on blog posts.
- `create_message_table.sql`: Sets up the `peeppea_message` table for storing system messages.
- `create_user_table.sql`: Sets up the `peeppea_user` table for storing user information.


---

# Deployment

## 1. Check Docker Setup

`docker-compose down` # Stop any running containers`
`docker-compose up --build` # Rebuild and start the containers

## 2. Run Database Connection Tests

`mvn clean test -Dspring.profiles.active=test`

## 3. Unit and Integration Tests

`mvn clean verify`

## 4. Build project

`mvn clean package -DskipTests`

## 5. Build docker image

`docker build -t PeepPeaDotCom .`

## 6. Test docker image 

`docker ps`
