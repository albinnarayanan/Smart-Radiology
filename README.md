Project Description:

This project is a full-stack healthcare workflow management application designed to streamline radiology operations by automating patient management, request scheduling, and report handling. It ensures secure, role-based access for doctors, technicians, and administrators while maintaining efficient data flow across the system.

Key Features:

- Patient management: Doctors can create new patient records and view complete patient details.
- Radiology request creation: Doctors can generate radiology requests for patients; reports are linked once technicians submit them.
- Automated scheduling: Radiology requests are automatically assigned to appropriate technicians based on request type.
- Technician workflows: Technicians receive imaging schedules, upload radiology images, and submit final reports.
- Report lifecycle: Doctors can view reports only after technicians submit them, ensuring proper workflow integrity.
- Admin dashboard: Administrators can access and manage details of doctors and technicians.
- Role-based dashboards: Distinct interfaces for doctors, technicians, and admins tailored to their responsibilities.
- JWT authentication: Secure token-based login with refresh token support for session reliability.
- Redis caching: Patient, doctor, and technician lists are cached for performance optimization, while dynamic radiology requests are handled via the database.

Tech Stack:

- Frontend: React, JavaScript
- Backend: Java, Spring Boot
- Database: MySQL
- Caching: Redis
- Authentication: JWT with refresh tokens
- API Testing: Postman
