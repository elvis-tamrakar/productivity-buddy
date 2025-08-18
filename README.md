# Productivity Buddy App

A social accountability productivity platform designed to help individuals reach their goals through peer motivation and daily check-ins. Think Duolingo meets habit tracking — with your real-life buddy pushing you to stay on track.

---

## 🚀 MVP Features

- **User Authentication**
  - Sign up, log in, log out securely with JWT
- **Goal Management**
  - Create personal goals with titles, descriptions, deadlines, and daily checkpoints
- **Buddy System**
  - Invite a buddy to track goals together and push each other daily
- **Daily Check-ins**
  - Update progress, receive and send nudges
- **Streaks & Reminders**
  - Maintain daily streaks, receive email reminders to log progress
- **Progress Tracking**
  - View your own and your buddy's streak and goal progress

---

## 🛠️ Tech Stack

| Layer        | Technology                     |
|--------------|--------------------------------|
| **Backend**  | Java 17+, Spring Boot          |
| **Database** | PostgreSQL                     |
| **Auth**     | Spring Security + JWT          |
| **Scheduling** | Spring Scheduler              |
| **Email**    | JavaMailSender                 |
| **Frontend** | Next.js 14 + TypeScript + Tailwind CSS |
| **State Management** | Zustand + React Query |
| **Build Tool** | Maven (Backend) + npm (Frontend) |
| **Hosting**  | (Planned) Render or Railway    |

---

## 📁 Project Structure

```
productivity-buddy/
├── Backend_ProductivityApp/     # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/example/productivity_app/
│   │       ├── controller/      # REST API Controllers
│   │       ├── service/         # Business Logic
│   │       ├── entity/          # Database Entities
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── config/          # Security & CORS Config
│   │       └── util/            # JWT Utilities
│   └── pom.xml
├── src/                         # Next.js Frontend
│   ├── app/                     # App Router Pages
│   ├── components/              # React Components
│   ├── lib/                     # API & Utilities
│   ├── store/                   # Zustand State Management
│   └── hooks/                   # Custom React Hooks
├── package.json                 # Frontend Dependencies
└── README.md                    # This file
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+** for backend
- **Node.js 18+** for frontend
- **PostgreSQL** database
- **Git**

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd Backend_ProductivityApp
   ```

2. **Configure database in `application.properties`:**
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/productivity_db
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

3. **Run the Spring Boot application:**
   ```bash
   ./mvnw spring-boot:run
   ```

   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd ..  # If you're in Backend_ProductivityApp
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm run dev
   ```

   The frontend will start on `http://localhost:3000`

### Database Setup

1. **Create PostgreSQL database:**
   ```sql
   CREATE DATABASE productivity_db;
   ```

2. **The application will automatically create tables on first run**

---

## 🔧 API Endpoints

### Authentication
- `POST /users/register` - User registration
- `POST /users/login` - User login

### Goals
- `GET /goals/user/{userId}` - Get user's goals
- `POST /goals/{userId}` - Create new goal
- `PUT /goals/{goalId}` - Update goal
- `DELETE /goals/{goalId}` - Delete goal
- `POST /goals/{goalId}/complete` - Complete goal

### Checkpoints
- `GET /checkpoints` - Get all checkpoints
- `POST /checkpoints` - Create checkpoint
- `PUT /checkpoints/{id}` - Update checkpoint
- `DELETE /checkpoints/{id}` - Delete checkpoint

### Buddy Requests
- `GET /buddies` - Get buddy requests
- `POST /buddies` - Create buddy request
- `PUT /buddies/{id}` - Update buddy request

---

## 🎨 Frontend Features

- **Modern UI** with Tailwind CSS
- **Responsive Design** for all devices
- **Real-time Updates** with React Query
- **Form Validation** with React Hook Form + Zod
- **Toast Notifications** for user feedback
- **Progress Charts** with Recharts
- **Authentication Guard** for protected routes

---

## 🔒 Security Features

- **JWT Authentication** with secure token handling
- **CORS Configuration** for cross-origin requests
- **Password Encryption** with BCrypt
- **Protected Routes** on frontend
- **Input Validation** on both frontend and backend

---

## 📱 Usage

1. **Register** a new account or **login** with existing credentials
2. **Create goals** with descriptions and deadlines
3. **Add checkpoints** to break down your goals
4. **Invite buddies** to collaborate and stay motivated
5. **Track progress** with visual charts and statistics
6. **Receive reminders** and stay on track

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- Built with [Next.js](https://nextjs.org/) and [Spring Boot](https://spring.io/projects/spring-boot)
- Styled with [Tailwind CSS](https://tailwindcss.com/)
- Icons from [Heroicons](https://heroicons.com/)
- Charts powered by [Recharts](https://recharts.org/)
