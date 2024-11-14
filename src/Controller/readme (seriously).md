DbInserter runs when an instance of Issue is created.

To run this code, go to MySQL workbench and change your root password to: KENDATABASE123
using the following SQL command:

    ALTER USER 'root'@'localhost' IDENTIFIED BY 'KENDATABASE123';


IMPORTANT; when running multiple queries at once in MySQL workbench,
deselect all text before pressing the 'lightning' button in the top left. (by pressing outside the code)
(for instance click on the schema)

Then, create a new schema called 'issuesdb', and run the following init SQL query:
to do this... click on schema (bottom left) then rightclick, then create schema, then name it 'issuesdb'
then click apply, then apply, then finish.
After make sure u are working in the schema by double clicking on it.
and run the following init SQL query:

    CREATE TABLE locations (
    location_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    road VARCHAR(50) NOT NULL,
    housenumber SMALLINT NOT NULL
    );
    
    
    CREATE TABLE categories (
    category_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(30) NOT NULL
    );
    INSERT INTO categories(category)
    VALUES('road'), ('vandalism'), ('electrical'), ('water'), ('obstruction'), ('other');
    
    CREATE TABLE statuses (
    status_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    status VARCHAR(30) NOT NULL
    );
    INSERT INTO statuses(status)
    VALUES('pending'), ('in_progress'), ('resolved');
    
    
    CREATE TABLE citizens(
    citizen_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    email TINYTEXT NOT NULL
    );
    
    
    CREATE TABLE issues (
    issue_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    location_id INT NOT NULL,
    `description` TEXT NOT NULL,
    category_id INT NOT NULL,
    status_id INT NOT NULL,
    citizen_id INT,
    
    FOREIGN KEY(location_id) REFERENCES locations(location_id),
    FOREIGN KEY(category_id) REFERENCES categories(category_id),
    FOREIGN KEY(status_id) REFERENCES statuses(status_id),
    FOREIGN KEY(citizen_id) REFERENCES citizens(citizen_id)
    );

------------------------------------------------------------------------------------

To reset the db, run this query, then run the init query again (you might have to disable safe mode).

    DROP TABLE issues, citizens, locations, categories, statuses;

To select relevant parts of the Controller:

    SELECT issues.issue_id, locations.road, locations.housenumber, locations.location_id, categories.category, `description`, statuses.status, citizens.email, citizens.citizen_id
    FROM issues
    INNER JOIN locations ON locations.location_id = issues.location_id
    INNER JOIN statuses ON statuses.status_id = issues.status_id
    INNER JOIN categories ON categories.category_id = issues.category_id
    LEFT OUTER JOIN citizens ON citizens.citizen_id = issues.citizen_id
    ORDER BY issue_id;