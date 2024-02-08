var Controller = Java.type("esa.mo.nmf.apps.FuzzyControl");
var Sleep = Java.type("java.lang.Thread");
var Verifier = Java.type("esa.mo.nmf.apps.Util");
var PID = Java.type("esa.mo.nmf.apps.PIDComparator");

Controller.log('Init experiment script');

Controller.log('Checking files integrity');
Sleep.sleep(5000);
Verifier.calculate_md5("Fuzzy_CP.fcl");
Verifier.calculate_md5("Fuzzy_LC.fcl");
Verifier.calculate_md5("Fuzzy_LE.fcl");
Verifier.calculate_md5("timeline.js");

Controller.log('Initializing parameters priority 2');
Controller.initiateFIS("Fuzzy_LC.fcl");
PID.setPIDGains(5.98e+0, 5.21e-2, 3.43e+2, 1.19e+1, 1.84e-1, 5.87e+2, 3.49e+0, 1.82e-1, 9.45e+1);

Controller.updateTLE();
Controller.log('Executing Exp 10');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 13');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 16');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000); 
Sleep.sleep(300000); 
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 11');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 12');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 14');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 15');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 17');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 18');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);
Controller.log('Initializing parameters priority 3');
Controller.initiateFIS("Fuzzy_LE.fcl");
PID.setPIDGains(4.54e+1, 9.43e-1, 3.55e+2, 1.34e+2, 7.77e-2, 5.94e+2, 9.55e+1, 2.61e-1, 2.47e+2);

Controller.updateTLE();
Controller.log('Executing Exp 19');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 20');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 21');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 22');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 23');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 24');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 25');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 26');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 27');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.log('Initializing parameters priority 1');
Controller.initiateFIS("Fuzzy_CP.fcl");
PID.setPIDGains(2.15e+1, 7.07e-1, 5.15e+2, 2.05e+1, 8.57e-1, 5.22e+2, 1.46e+1, 9.95e-1, 1.74e+2);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(120000);

Controller.updateTLE();
Controller.log('Executing Exp 1');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, -0.04362, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 2');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, -0.04362, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 3');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, 0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99905, 0.00000, 0.00000, -0.04362);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 4');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, -0.08716, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 5');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, -0.08716, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 6');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, 0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99619, 0.00000, 0.00000, -0.08716);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 7');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, -0.13053, 0.00000, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 8');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000);   
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, -0.13053, 0.00000); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 9');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, 0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99144, 0.00000, 0.00000, -0.13053);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.log('Initializing experiments priority 4');

Controller.initiateFIS("Fuzzy_CP.fcl");
PID.setPIDGains(2.15e+1, 7.07e-1, 5.15e+2, 2.05e+1, 8.57e-1, 5.22e+2, 1.46e+1, 9.95e-1, 1.74e+2);

Controller.updateTLE();
Controller.log('Executing Exp 28');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 29');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 30');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.initiateFIS("Fuzzy_LC.fcl");
PID.setPIDGains(5.98e+0, 5.21e-2, 3.43e+2, 1.19e+1, 1.84e-1, 5.87e+2, 3.49e+0, 1.82e-1, 9.45e+1);

Controller.updateTLE();
Controller.log('Executing Exp 31');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 32');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 33');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.initiateFIS("Fuzzy_LE.fcl");
PID.setPIDGains(4.54e+1, 9.43e-1, 3.55e+2, 1.34e+2, 7.77e-2, 5.94e+2, 9.55e+1, 2.61e-1, 2.47e+2);

Controller.updateTLE();
Controller.log('Executing Exp 34');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99723, 0.04164, 0.04544, 0.04164);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.99706, -0.04544, -0.04164, -0.04544);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 35');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98929, 0.07893, 0.09406, 0.07893);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.98797, -0.09406, -0.07893, -0.09406);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.updateTLE();
Controller.log('Executing Exp 36');
Controller.desaturateWheels();
while (Controller.desaturating){
    Sleep.sleep(1000);
};
Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141); 
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97678, 0.11141, 0.14519, 0.11141);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("Fuzzy");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);  
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);

Controller.setControllerType("PID");
Controller.setTargetAttitude(0.97233, -0.14519, -0.11141, -0.14519);
Sleep.sleep(300000);
Controller.setTargetAttitude(1.0, 0.0, 0.0, 0.0);
Sleep.sleep(300000);
