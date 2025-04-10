drop database pharmacyDB;
create database pharmacyDB;

use pharmacyDB;
show tables;
CREATE TABLE supplier_Company (
    company_name VARCHAR(70) PRIMARY KEY NOT NULL,
    phone INT,
    address VARCHAR(150)
);
CREATE TABLE categores (
    cat_id INT PRIMARY KEY NOT NULL,
    categores_name VARCHAR(70),
    number_of_item INT 
);
CREATE TABLE item (
  	item_name VARCHAR(90),
    par_code INT NOT NULL,
     quantity INT,
    discription VARCHAR(800),
    sale_price REAL,
    cost_price REAL,
	supplier_company_name VARCHAR(70),
    cat_id INT,
    exp_date DATE,
    PRIMARY KEY (par_code , supplier_company_name , cat_id),
    FOREIGN KEY (supplier_company_name)
        REFERENCES supplier_Company (company_name)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (cat_id)
        REFERENCES categores (cat_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE employee (
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    employeeName VARCHAR(65),
	username VARCHAR(45),
    birthday DATE,
    dateOfEmployment DATE,
    empPasswd VARCHAR(30)
);

-- CREATE TABLE user_account (
--    id_account INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
--    firstName VARCHAR(45) NOT NULL,
--    LastName VARCHAR(45) NOT NULL,
--    userName VARCHAR(45) NOT NULL,
--    empPasswd VARCHAR(30) NOT NULL
-- );

CREATE TABLE hourly_employee (
    id INT PRIMARY KEY,
    workHours int,
    hourPrice int,
    FOREIGN KEY (id)
        REFERENCES employee (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE contrect_employee (
    id INT PRIMARY KEY,
    amountPaid REAL,
    FOREIGN KEY (id)
        REFERENCES employee (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE orders (
    order_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id INT,
    FOREIGN KEY (id)
        REFERENCES employee (id)
        ON DELETE NO ACTION ON UPDATE CASCADE
);

CREATE TABLE cash_Order (
    order_id INT PRIMARY KEY,
    order_date DATE,
    discount int,
    FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE inshurance_Company (
    inshurance_companyName VARCHAR(32) PRIMARY KEY,
    inshurance_companyDiscount INT,
    numberOfCustomer INT DEFAULT 0
);
CREATE TABLE inshurance_Customer (
    coustumerID INT PRIMARY KEY,
    coustumerName VARCHAR(32),
    inshurance_companyName VARCHAR(32) NOT NULL,
    FOREIGN KEY (inshurance_companyName)
        REFERENCES inshurance_Company (inshurance_companyName)
        ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE TABLE inshurance_Order (
    coustumer_inshurance_id INT NOT NULL,
    order_date DATE,
    order_id INT,
    PRIMARY KEY (coustumer_inshurance_id , order_id),
    FOREIGN KEY (coustumer_inshurance_id)
        REFERENCES inshurance_Customer (coustumerID)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE payment_Detail (
    order_id INT PRIMARY KEY NOT NULL,
    order_date DATE,
    totalPrice REAL,
    profits REAL,
    emp_id INT,
    FOREIGN KEY (emp_id)
        REFERENCES employee (id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE NO ACTION ON UPDATE CASCADE
);
CREATE TABLE invoice (
    quantity INT,
    total_Sale_Price double,
    total_cost_price double,
    par_code INT NOT NULL,
    order_id INT,
    PRIMARY KEY (par_code , order_id),
    FOREIGN KEY (order_id)
        REFERENCES orders (order_id)
        ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (par_code)
        REFERENCES item (par_code)
        ON DELETE NO ACTION ON UPDATE CASCADE
);

insert into employee (employeeName,username,birthday,dateOfEmployment,empPasswd) value ("Omar Mutan","omarMutan2",'1985-01-15','2010-05-16',"admin");


insert into supplier_company values ("AL_Quds",022406550,"Nablus Street - Al Baloua - Al-Bireh - Ramallah / Palestine"),
("Birzeit",022987572,"Rel'jefna Street - Ramallah / Palestine"),
("Beit_Jalla",022890447,"Beit_Jalla - hadera street"),("Al_Sharqea",022812586,"Al_Bera - Ramallah / Palestine"),
("Jama",022746892,"Betonea - Ramallah / Palestine"),("Dar_Alshefa",022478916,"Betonea - Ramallah / Palestine");

insert into categores values (110,"sedatives",3),(120,"heart medications",3),(113,"antibacterial drugs",2),(096,"Mycobacterial drugs",2),(118,"monocytes drugs",1),
 (240,"deworming drugs",2),(201,"antiviral drugs",1),(047,"cancer drugs",2),(014,"breakfast medicines",0);

 insert into employee (employeename,username,birthday,dateofemployment,empPasswd) values ("Seraj Mustafa Hijaz","serajMH222",'1992-01-15','2018-05-16',"root"),
 ("Sawsan Ahmad Shekha","sawsanAhmadShekha",'1997-08-26','2020-08-14',"root1"),("Allah Bajes Zaben","alaahBBB",'1995-04-21','2021-08-14',"root2"),
 ("Lena Hatem Hamed","123LenaHatem",'1990-05-26','2019-01-30',"root3"),("Saja Yaser Ayyad","sajaAyyad",'1980-08-26','2012-12-14',"root4"),
 ("Yusra Ahmad Rimawi","yusraRimawi3",'1992-07-04','2018-01-25',"root5"),("Khaled Mohammad Fattah","khaledFathallah",'1995-08-26','2020-08-14',"root6"),
 ("Mohammad Ahmad Asfour","mohammad1234",'1989-12-26','2015-02-14',"root7"),("Ahmad Malik Alwan","ahmad444",'1991-08-26','2018-07-14',"root8"); 

 insert into hourly_employee values (3,8,15),(4,5,18),(6,8,17),(7,4,11),(10,10,22);
 insert into contrect_employee value (2,4800),(5,3700),(8,2850),(9,3210);
 insert into item values ("Acamol",100,15,"Antipyretic and analgesic for pain accompanied by insomnia",25,15,"AL_Quds",110, '2022-04-15'),
      ("acebutolol",101,27,"Beta blockers reduce the heart rate and reduce irregularity",34,22,"Birzeit",120, '2022-03-25'),
      ("Dabigatran", 102,40,"used to prevent strokes in those with atrial fibrillation not caused by heart valve issues",40,37,"AL_Quds",120,'2022-01-17'),
      ("Edoxaban",103,10,"preventing blood clots in people with nonvalvular atrial fibrillation who also have at least one risk factor",55,51,"Birzeit",120, '2022-6-10'),
      ("Bendamustine (Levact)",104,22," chemotherapy medication used in the treatment of chronic lymphocytic leukemia (CLL)",41,35,"AL_Quds",47, '2022-12-12'),
      ("Bevacizumab (Avastin)",105,5,"used for Colorectal cancer, Lung cancer, Breast cancer, Renal cancers, Brain cancers, Eye disease, Ovarian cancer, Cervical cancer and Administration",90,80,"Birzeit",47, '2023-07-11'),
      ("rifampin",106,50,"used for the treatment of tuberculosis in combination with other antibiotics, such as pyrazinamide, isoniazid, and ethambutol.",64,52,"AL_Quds",96, '2024-09-10'),
      ("Isoniazid (INH)",107,71,"Bactericidal • primary drug for LTBI and a primary drug for use in combinations",9,5,"Birzeit",96, '2024-02-01'),
      ("diazepam (Valium)",108,10,"used to treat anxiety, insomnia, panic attacks and symptoms of acute alcohol withdrawal",62,54,"Birzeit",110, '2023-07-02'),
      ("alprazolam (Xanax)",109,32,"used to treat panic disorders and generalized anxiety",14,10,"Birzeit",110, '2024-11-15'),
      ("amoxicillin",110,6,"used to treat the symptoms of many different types of bacterial infections",36,24,"Birzeit",113, '2022-04-17'),
      ("doxycycline",111,124,"indicated for treatment of infections caused by the following microorganisms",47,35,"Birzeit",113, '2024-12-04'),
      ("Infliximab",112,65,"used to treat the symptoms of Rheumatoid Arthritis, Psoriatic Arthritis, Plaque Psoriasis, Chron Disease, Ulcerative Colitis and Ankylosing Spondylitis",27,20,"Birzeit",118, '2022-08-21'),
      ("Penciclovir",113,24,"used for the treatment of various herpes simplex virus (HSV) infections",33,24,"AL_Quds",201, '2023-04-30'),
      ("Peramivir",114,27,"treatment of acute uncomplicated influenza in adults",63,45,"Birzeit",201, '2024-01-26'),
      ("Levamisole",115,54,"used to treat helminth infections and some skin infections",30,21,"AL_Quds",240, '2023-07-14'),
      ("Niclosamide",116,12,"used to treat parasitic infections in millions of people worldwide",60,54,"AL_Quds",240, '2022-02-23');
   -- delete from  item where par_code = 114 And supplier_company_name = "Birzeit" AND cat_id = 201;
	
 
 insert into inshurance_company values("Al Mashreq",10,2),("Bank of Palestine",15,2),("islamic Bank",20,1),
 ("Al-Takaful",10,2);
 insert into inshurance_company values("Birziet University",12,2);
 insert into inshurance_customer values(5835,"Dr.Bassem Sayrafi","Al Mashreq"),(1210834,"Yousra Issa","Al Mashreq"),
 (1210649,"Ayah Hammad","Bank of Palestine"),(1201466,"Lana Zaim","Bank of Palestine"),(1210768,"Aya Hijaz","Al-Takaful"),(1208293,"Saeed Limon","Al-Takaful")
 ,(1220485,"Mohammad Nael","islamic Bank");
    
    
	  show tables; 
      select * from payment_Detail;
      select * from cash_order;
      select * from categores;
	  select * from employee;
      select * from contrect_employee;
      select * from hourly_employee;
      select * from inshurance_customer;
      select * from inshurance_company;
      select * from inshurance_order;
      select * from invoice;
      select * from item;
      select * from orders;
      select * from supplier_company;
      





