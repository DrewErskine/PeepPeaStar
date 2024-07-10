# Overview
PeepPeaStar is a Spring Boot application designed for managing blog content, user authentication, and character profiles. The application has a playful theme centered around "Peepville" characters, offering various features for users to explore blogs, characters, and other related content.

# Key Features
- **Blog Management**: Create, read, and list blogs with support for comments and user interactions.
- **User Authentication**: Registration, login, and authentication using Spring Security.
- **Character Profiles**: Display detailed profiles of "Peepville" characters.
- **Responsive UI**: Front-end designed with HTML, CSS, and Thymeleaf templates for a responsive user experience.
- **Database Integration**: Stores user, blog, and comment data in a MySQL database.

# Project Structure
- **Main Application**: `PeepPeaStarApplication.java` and `ServletInitializer.java`
- **Beans**: `Blog.java`, `User.java`
- **Configuration**: `SecurityConfig.java`
- **Controllers**: `BlogController.java`, `HomeController.java`, `UserController.java`
- **DAOs**: `BlogRepository.java`, `UserRepository.java`
- **DTOs**: `UserRegistrationDto.java`
- **Resources**:
  - **Static Files**: CSS, images, fonts, JavaScript
  - **Templates**: HTML templates for various pages (`index.html`, `blog-list.html`, `register-user.html`, etc.)
- **Test**: `PeepPeaStarApplicationTests.java`

# Beans
- **Blog**: Represents blog entries with fields for title, content, user, etc.
- **User**: Represents users with fields for name, email, password, etc.

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
- **Database Files**: Contains SQL files for initializing the database tables for blogs, comments, and users.

# SQL Files
- **peepepea_blog**: Table structure for storing blog posts.
- **peeppea_comments**: Table structure for storing comments on blog posts.
- **peeppea_user**: Table structure for storing user information.
