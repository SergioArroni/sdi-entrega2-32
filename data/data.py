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
'''

# print("Data inserted with record ids",rec_prod_id1," ",rec_prod_id2)

# insert users

collection = db.user

user1 = {
		"id": str(uuid.uuid4()),
		"username":"LeBron Raymone James Sr.",
		"email":"Thegoat@gmail.com",
        "salt": "596c06198d4ad5e349aab6de4e38c7a4",
        "hash": "55e2bf53ca5eecea3c225c6164abde0c008f86b5da63d68a52432025af08c134315359614b8a9f12a54917a539bc07636173a4ac9775be392d022ef748b07e0e",
		"rol":"Admin"
		}
user2 = {
		"id": str(uuid.uuid4()),
		"username":"Wardell Stephen Curry II",
		"email":"Chef@gmail.com",
     	"salt": "339ca94acee3deca6d7b828d32ec2c91",
        "hash": "dbca9157ddd245919d7d8f1447de16038fef80c6452654385257d3a32cc2b512f1fb2199df14248d6d6224ea8eb7eb783743038fa01d3ce2d5e07307b2517c74",
		"rol":"Client"
		}
user3 = {
		"id": str(uuid.uuid4()),
		"username":"Benjamin David Simmons",
		"email":"Ben@gmail.com",
        "salt": "00c5983ba369ba2bc1af6e0c097c8cc9",
        "hash": "6ee29247f6d06d7b1bfd550766c5d8ca4d0454deecfd2049cb4a9450ee823ec2ce5d0925fb91355ebc8423ac07cf87e70f66ec84c1f32c8dce097358a98bab5c",
		"rol":"Client"
		}
user4 = {
		"id": str(uuid.uuid4()),
		"username":"Dwyane Tyrone Wade, Jr.",
		"email":"Dwyane@gmail.com",
        "salt": "37028d151dd80a66e0228630712fd781",
        "hash": "81d2ab8889ecb3c4841a14f4cb2e45364f622405be31a1593f4f9ae373d62b376e64ab5392bbbbb6061f5cbe1f682562369d78720fab687959b45dcd7465b5b6",
		"rol":"Client"
		}
user5 = {
		"id": str(uuid.uuid4()),
		"username":"Russell Westbrook III",
		"email":"West@gmail.com",
        "salt": "f28fef0fc80e40a304ed402c8d3a8adc",
        "hash": "ca9cc4ac3f99fce105b6502fa4756dc77704fb6289ccbad485f414a47688983da4e62653601320c808e5066a1ab5f49652a89b6b9ea0e1c09d7173481b01b1d2",
		"rol":"Client"
		}

# Insert Data
rec_user_id1 = collection.insert_one(user1)
rec_user_id2 = collection.insert_one(user2)
rec_user_id3 = collection.insert_one(user3)
rec_user_id4 = collection.insert_one(user4)
rec_user_id5 = collection.insert_one(user5)


# print("Data inserted with record ids",rec_user_id1," ",rec_user_id2)


collection = db.distribution_center

dc1 = {
		"id": dc1_id,
		"address": "Calle Valdes Salas, 11, 33007 Oviedo, Asturias"
}
dc2 = {
		"id": dc2_id,
		"address": "Plaza de la Independencia, 7, 28001 Madrid"
}	
# Insert Data
rec_distcenter_id1=collection.insert_one(dc1)
rec_distcenter_id2=collection.insert_one(dc2)

collection = db.product_store

ps1_1 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod1_id,
	"stock": 6
}
ps1_2 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod2_id,
	"stock": 10
}
ps1_3 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod3_id,
	"stock": 34
}
ps1_4 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod4_id,
	"stock": 22
}
ps1_5 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod5_id,
	"stock": 3
}
ps1_6 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod6_id,
	"stock": 40
}
ps1_7 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod7_id,
	"stock": 6
}
ps1_8 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod8_id,
	"stock": 8
}
ps1_9 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod9_id,
	"stock": 6
}
ps1_10 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod10_id,
	"stock": 12
}
ps1_11 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod11_id,
	"stock": 20
}
ps1_12 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod12_id,
	"stock": 15
}
ps1_13 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod13_id,
	"stock": 11
}
ps1_14 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod14_id,
	"stock": 7
}
ps1_15 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod15_id,
	"stock": 8
}
ps1_16 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod16_id,
	"stock": 10
}
ps1_17 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod17_id,
	"stock": 4
}
ps1_18 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod17_id,
	"stock": 6
}
ps1_23 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod23_id,
	"stock": 23
}
ps1_24 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod24_id,
	"stock": 21
}
ps1_25 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod25_id,
	"stock": 17
}
ps1_26 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc1_id,
	"product_id": prod26_id,
	"stock": 15
}
ps2_1 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod1_id,
	"stock": 2
}
ps2_3 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod3_id,
	"stock": 8
}
ps2_4 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod4_id,
	"stock": 4
}
ps2_5 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod5_id,
	"stock": 10
}
ps2_8 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod8_id,
	"stock": 5
}
ps2_9 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod9_id,
	"stock": 14
}
ps2_17 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod17_id,
	"stock": 14
}
ps2_19 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod19_id,
	"stock": 20
}
ps2_20 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod20_id,
	"stock": 10
}
ps2_21 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod21_id,
	"stock": 14
}
ps2_22 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod22_id,
	"stock": 14
}
ps2_23 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod23_id,
	"stock": 14
}
ps2_25 = {
	"id": str(uuid.uuid4()),
	"distributioncenter_id": dc2_id,
	"product_id": prod25_id,
	"stock": 22
}
collection.insert_one(ps1_1)
collection.insert_one(ps1_2)
collection.insert_one(ps1_3)
collection.insert_one(ps1_4)
collection.insert_one(ps1_5)
collection.insert_one(ps1_6)
collection.insert_one(ps1_7)
collection.insert_one(ps1_8)
collection.insert_one(ps1_9)
collection.insert_one(ps1_10)
collection.insert_one(ps1_11)
collection.insert_one(ps1_12)
collection.insert_one(ps1_13)
collection.insert_one(ps1_14)
collection.insert_one(ps1_15)
collection.insert_one(ps1_16)
collection.insert_one(ps1_17)
collection.insert_one(ps1_18)
collection.insert_one(ps1_23)
collection.insert_one(ps1_24)
collection.insert_one(ps1_25)
collection.insert_one(ps1_26)
collection.insert_one(ps2_1)
collection.insert_one(ps2_3)
collection.insert_one(ps2_4)
collection.insert_one(ps2_5)
collection.insert_one(ps2_8)
collection.insert_one(ps2_9)
collection.insert_one(ps2_17)
collection.insert_one(ps2_19)
collection.insert_one(ps2_20)
collection.insert_one(ps2_21)
collection.insert_one(ps2_22)
collection.insert_one(ps2_23)
collection.insert_one(ps2_25)

collection = db.productorder

prodord1 = {
		"id": str(uuid.uuid4()),
		"product": prod1,
		"quantity": 5,
		"shippingPrice": 1,
		"distributionCenter": dc1
		}
prodord2 = {
		"id": str(uuid.uuid4()),
		"product": prod3,
		"quantity": 2,
		"shippingPrice": 3,
		"distributionCenter": dc1
		}
prodord3 = {
		"id": str(uuid.uuid4()),
		"product": prod9,
		"quantity": 6,
		"shippingPrice": 1.5,
		"distributionCenter": dc1
		}
# Insert Data
rec_prodorder_id1=collection.insert_one(prodord1)
rec_prodorder_id2=collection.insert_one(prodord2)

collection = db.order

ord1 = {
		"id": str(uuid.uuid4()),
		"user": "Thegoat@gmail.com",
		"products": [prodord1,prodord2]
		}

# Insert Data
rec_order_id1=collection.insert_one(ord1)
'''