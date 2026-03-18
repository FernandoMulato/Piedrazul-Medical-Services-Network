import sqlite3

def check():
    conn = sqlite3.connect('database.db')
    cursor = conn.cursor()
    cursor.execute("SELECT * FROM APPOINTMENT WHERE APP_ID=1")
    row = cursor.fetchone()
    print("Row from python:", row)
    print("Columns:", [description[0] for description in cursor.description])

if __name__ == "__main__":
    check()
