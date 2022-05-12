# Python code to illustrate
# inserting data in MongoDB
from pymongo import MongoClient
import uuid
import os

try:
	conn = MongoClient("mongodb://SergioArroni:julio321@mongo:27017/SDI?authSource=admin&readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false")
	print("Connected successfully!!!")
except:
	print("Could not connect to MongoDB")

# database
db = conn.SDI

# Created or Switched to collection names: my_gfg_collection
collection = db.users

admin = {
    "_id": {"$oid":"625f3514ec93e8d1e4a0d938"}
    ,"email":"admin@email.com"
    ,"name":"Arthas"
    ,"surname":"Menethil"
    ,"password":"ebd5359e500475700c6cc3dd4af89cfd0569aa31724a1bf10ed1e3019dcfdb11"
    ,"rol":"Admin"
    }

# Insert Data
admin_a = collection.insert_one(admin)
