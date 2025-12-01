# CourseTrack Server Documentation
## Requests

#### User Registration

**Message Type:** `USER_REGISTER`

**Request:** `RegisterRequest`
- `username` (String): Desired username
- `password` (String): Desired password
- `type` (UserType): Type of user (ADMIN or STUDENT)

**Response:** `RegisterResponse`
- Empty response on success
- Error message on failure

**Status Codes:**
- `SUCCESS`: Registration successful
- `FAILURE`: Username exists, invalid password, or user already logged in

**Validation Rules:**
- Username must not already exist
- Password must meet validation requirements (see `User.validatePassword()`)

---

#### User Login

**Message Type:** `USER_LOGIN`

**Request:** `LoginRequest`
- `username` (String): User's username
- `password` (String): User's password

**Response:** `LoginResponse`
- **Success:** Returns client user object with user type
- **Failure:** Returns null if credentials are invalid or user already logged in

**Status Codes:**
- `SUCCESS`: Login successful
- `FAILURE`: Invalid credentials or already logged in

---

#### User Logout

**Message Type:** `USER_LOGOUT`

**Request:** `LogoutRequest`

**Response:** `LogoutResponse`

**Status Codes:**
- `SUCCESS`: Logout successful

---

#### Change Password

**Message Type:** `USER_CHANGE_PASSWORD`

**Request:** `PasswordChangeRequest`
- `password` (String): New password

**Response:** `PasswordChangeResponse`

**Status Codes:**
- `SUCCESS`: Password updated
- `FAILURE`: Invalid password or user not logged in

**Requirements:**
- User must be logged in
- Password must meet validation requirements

---

### Administration

All administrative operations require admin privileges. If validation fails, a FAILURE response is returned.

#### Add Campus

**Message Type:** `ADMIN_ADD_CAMPUS`

**Request:** `AddCampusRequest`
- `campus` (String): Campus name

**Response:** `AddCampusResponse`

**Status Codes:**
- `SUCCESS`: Campus created
- `FAILURE`: Campus already exists or insufficient permissions

---

#### Add Department

**Message Type:** `ADMIN_ADD_DEPARTMENT`

**Request:** `AddDepartmentRequest`
- `name` (String): Department name
- `campus` (String): Campus name

**Response:** `AddDepartmentResponse`

**Status Codes:**
- `SUCCESS`: Department created
- `FAILURE`: Campus not found, department already exists, or insufficient permissions

---

#### Add Course

**Message Type:** `ADMIN_ADD_COURSE`

**Request:** `AddCourseRequest`
- `name` (String): Course name
- `number` (int): Course number
- `units` (int): Credit units
- `campus` (String): Campus name
- `department` (String): Department name

**Response:** `AddCourseResponse`
- Returns the created `Course` object on success
- Returns null on failure

**Status Codes:**
- `SUCCESS`: Course created
- `FAILURE`: Campus not found, department not found, course already exists, or insufficient permissions

---

#### Add Section

**Message Type:** `ADMIN_ADD_SECTION`

**Request:** `AddSectionRequest`
- `capacity` (int): Maximum enrollment capacity
- `courseId` (String): Course identifier
- `term` (String): Term identifier
- `campus` (String): Campus name
- `department` (String): Department name
- `instructor` (String): Instructor name
- `meetTimes` (Object): Meeting times schedule

**Response:** `AddSectionResponse`
- Returns the created `Section` object on success
- Returns null on failure

**Status Codes:**
- `SUCCESS`: Section created
- `FAILURE`: Campus/department/course not found, section already exists, or insufficient permissions

---

### Students

All student operations require student privileges. If validation fails, a FAILURE response is returned.

#### Browse Sections

**Message Type:** `STUDENT_BROWSE_SECTION`

**Request:** `BrowseSectionRequest`
- `searchQuery` (String): Search term
- `campus` (String): Campus name
- `department` (String): Department name
- `term` (String): Term identifier
- `max_results` (int): Maximum number of search results to return

**Response:** `BrowseSectionResponse`
- Returns list of matching `Section` objects

**Status Codes:**
- `SUCCESS`: Search completed (may return empty list)
- `FAILURE`: Insufficient permissions

**Search Behavior:**
- Search by term
- Returns null list if parameters are invalid

---

#### Enroll in Section

**Message Type:** `STUDENT_ENROLL`

**Request:** `EnrollSectionRequest`
- `sectionId` (int): Section identifier
- `term` (String): Term identifier

**Response:** `EnrollSectionResponse`
- `section` (Section): Enrolled section object (Section = null if waitlisted)
- `waitlistPosition` (int): Waitlist position (Position = 0 if not waitlisted)

**Status Codes:**
- `SUCCESS`: Enrolled or added to waitlist
- `FAILURE`: Prerequisites not met, schedule conflict, or insufficient permissions

**Validation:**
1. Verifies student meets course prerequisites
2. Checks for schedule conflicts with currently enrolled sections
3. If section is full, adds student to waitlist

---

#### Drop Section

**Message Type:** `STUDENT_DROP`

**Request:** `DropSectionRequest`
- `sectionId` (int): Section identifier
- `term` (String): Term identifier

**Response:** `DropSectionResponse`

**Status Codes:**
- `SUCCESS`: Section dropped
- `FAILURE`: Insufficient permissions

**Side Effects:**
- Removes student from section
- If waitlist exists, promotes first waitlisted student
- Notifies promoted student

---

#### Get Schedule

**Message Type:** `STUDENT_GET_SCHEDULE`

**Request:** `GetScheduleRequest`

**Response:** `GetScheduleResponse`
- Returns list of currently enrolled `Section` objects

**Status Codes:**
- `SUCCESS`: Schedule retrieved
- `FAILURE`: Insufficient permissions

---

## Example

### Connect to server

```java
Client client = new Client("localhost", 7777);
ClientController controller = new ClientController(client);

if (!client.isConnected()) {
    if (!client.connect()) {
        // Client failed to connect
    }
}

// Start client listener
controller.start();
```

### Send a request

```java
client.sendRequest(new Message<RegisterRequest>(MessageType.USER_REGISTER, MessageStatus.REQUEST, new RegisterRequest(username, password, type) ));
```

```java
client.sendRequest(new Message<LoginRequest>(MessageType.USER_LOGIN, MessageStatus.REQUEST, new LoginRequest(username, password) ));
```

Or with an IAppGuiService:
```java
guiService.sendRequest(MessageType.USER_REGISTER, MessageStatus.REQUEST, new RegisterRequest(username, password, type) );
```