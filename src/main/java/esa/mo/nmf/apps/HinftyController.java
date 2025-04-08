package esa.mo.nmf.apps;

import org.hipparchus.linear.Array2DRowRealMatrix;
import org.hipparchus.linear.RealMatrix;

public class HinftyController {

    // Define system matrices A, B, C, and D
    private RealMatrix A; // State matrix
    private RealMatrix B; // Input matrix
    private RealMatrix C; // Output matrix
    private RealMatrix D; // Feedthrough matrix
    private RealMatrix x; // State Vector

    // Constructor to initialize matrices (you can set your own values)
    public HinftyController(RealMatrix A, RealMatrix B, RealMatrix C, RealMatrix D) {
        // Initialize A, B, C, and D matrices
        // Example: A = new Array2DRowRealMatrix(...);           
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
    }

    public HinftyController(String defaultCOntroller) {
        switch (defaultCOntroller) {
            case "defaultX":
                double[][] matrixDataAx = {
                    {-0.85963, -0.38075, 0.00067361, 0.00013096, 9.2567e-7, 0.0054525, 0.080244},
                    {1, -0.22664, 0, 0.0024511, 0, 0, 0},
                    {0, -28.755, -2.2806, -0.035094, 0, 0, 6.9657},
                    {-0.00011621, -2.876, 2.1059e-10, 0.030834, -6.9306e-7, 3.9463e-7, 1.1639e-6},
                    {3.5226e-5, -0.36553, -1.3447e-11, 0.0039531, -0.022805, -4.5899e-8, -1.1113e-7},
                    {0, -0.16669, 0, 0.0031545, 0., -0.05832, -0.02721},
                    {0, 0.041396, 0, -0.00044768, 0, 0.03125, 0}};
                this.A = new Array2DRowRealMatrix(matrixDataAx);
                
                double[][] matrixDataBx = {
                    {-0.011264},
                    {-0.22664},
                    {3.2451},
                    {-2.876},
                    {-0.36553},
                    {-0.16669},
                    {0.041396}};
                this.B = new Array2DRowRealMatrix(matrixDataBx);
        
                double[][] matrixDataCx = {{36560., 15714., -28.649, -0.38867, 30.162, -231.90, -3412.8}};
                this.C = new Array2DRowRealMatrix(matrixDataCx);
        
                double[][] matrixDataDx = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDx);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

            case "defaultY":
                double[][] matrixDataAy = {
                    {-0.92910, -0.44486, 0.00081, 0.00015, 1.45607e-6, 0.00592, 0.09377},
                    {1, -0.23493, 0, 0.00254, 0, 0, 0},
                    {0, -29.11553, -2.65599, -0.03119, 0, 0, 6.96570},
                    {-8.57454e-5, -2.70921, 1.38017e-10, 0.02903, -7.03171e-7, 2.91427e-7, 8.65292e-7},
                    {3.52173e-5, -0.35911, -1.60045e-11, 0.00388, -0.02656, -5.41514e-8, -1.36973e-7},
                    {0, -0.15287, 0, 0.00301, 0, -0.05832, -0.02721},
                    {0, 0.04146, 0, -0.00045, 0, 0.03125, 0}};
                this.A = new Array2DRowRealMatrix(matrixDataAy);
                
                double[][] matrixDataBy = {
                    {-0.01325},
                    {-0.23493},
                    {2.88447},
                    {-2.70921},
                    {-0.35912},
                    {-0.15287},
                    {0.04146}};
                this.B = new Array2DRowRealMatrix(matrixDataBy);
        
                double[][] matrixDataCy = {{33929.42613, 15761.91047, -29.40387, -0.33401, 35.11965, -216.36715, - 3424.30149}};
                this.C = new Array2DRowRealMatrix(matrixDataCy);
        
                double[][] matrixDataDy = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDy);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

                case "defaultZ":
                double[][] matrixDataAz = {
                    {-1.73155, -1.56212, 0.00161, 0.00069, 1.39070e-5, 0.01138, 0.32614},
                    {1, -0.41056, 0, 0.00444, 0, 0, 0},
                    {0, -61.10315, -9.09051, -0.03133, 0, 0, 13.93141},
                    {-7.34620e-6, -2.11150, 2.10757e-12, 0.02257, -1.30496e-7, 2.50605e-8, 7.75549e-8},
                    {0.00014, -1.44758, -1.21908e-10, 0.01566, -0.09090, -5.45347e-7, -2.35077e-6},
                    {0, -0.09162, 0, 0.00234, 0, -0.05832, -0.02721},
                    {0, 0.03546, 0, -0.00038, 0, 0.03125, 0}};
                this.A = new Array2DRowRealMatrix(matrixDataAz);
                
                double[][] matrixDataBz = {
                    {-0.06299},
                    {-0.41056},
                    {2.89685},
                    {-2.11150},
                    {-1.44759},
                    {-0.09162},
                    {0.03546}};
                this.B = new Array2DRowRealMatrix(matrixDataBz);
        
                double[][] matrixDataCz = {{18475.13552, 15995.28487, -17.17108, -0.09796, 29.94757, -121.44778, -3479.85365}};
                this.C = new Array2DRowRealMatrix(matrixDataCz);
        
                double[][] matrixDataDz = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDz);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

                case "newX":
                double[][] matrixDataAxnew = {
                   { -0.150740235373300,	0,	-0.0290450137904789,	0,	0.000767492076447206,	0.00915595947505277,	0,	0,	0}, 
                   { 2.80417747082691e-08,	-0.135352553094353,	-0.0895551790129072,	-3.06464639699921e-07,	0.00217278754671538,	-2.43098769419429e-07,	0.000573883502502763,	0.00117246952832939,	2.88215816770978e-06}, 
                   { 0,	1,	-1.18001183493949,	0,	0.0311809021666522,	0,	0,	0,	0}, 
                   { 0,	0,	-3.32676247615280,	-9.24445290663519,	-0.0177897820550887,	0,	0,	0.640000000000000,	0}, 
                   { -1.44961522413147e-07,	-0.496512184911499,	-23.9543203793816,	-7.47783970833964e-09,	0.632523559941684,	3.57199787919248e-07,	0.00166289430657098,	0.00263590614518027,	-0.000126736459055261}, 
                   { -0.000613448791215453,	0.00373617396827751,	-0.479817321563359,	-1.86048260611071e-12,	0.0126786313273267,	-0.00205735732743293,	7.18643250113436e-07,	1.04566556205692e-06,	3.83180750692949e-06}, 
                   { 0,	0,	-2.75718922476799,	0,	0.0794626547274499,	0,	-0.100000000000000,	-0.0400000000000000,	0}, 
                   { 0,	0,	2.38508711878678,	0,	-0.0630240866301536,	0,	0.0625000000000000,	0,	0}, 
                   { -8.07710751678619e-06,	9.97527927311563,	-47.5117162569597,	1.25424092761654e-05,	1.26452926058545,	0.00269501825390817,	-0.0282970196531100,	-0.0549068202632271,	-0.0334446029274320}};
                this.A = new Array2DRowRealMatrix(matrixDataAxnew);
                
                double[][] matrixDataBxnew = {
                   { -0.0290450137904789}, 
                   { -0.0805177175076107}, 
                   { -1.18001183493949}, 
                   { 0.673237523847204}, 
                   { -23.9298297979021}, 
                   { -0.479810557044230}, 
                   { -2.75718922476799}, 
                   { 2.38508711878678}, 
                   { -47.9798308969507}};
                this.B = new Array2DRowRealMatrix(matrixDataBxnew);
        
                double[][] matrixDataCxnew = {{-0.00130670831649793,	5897.46849892239,	390.977011455339,	0.0132111600841175,	-1.96766825164223,	0.695026502719208,	-24.8070663848165,	-50.6409048212102,	1.53417531381933}};
                this.C = new Array2DRowRealMatrix(matrixDataCxnew);
        
                double[][] matrixDataDxnew = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDxnew);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

                case "newY":
                double[][] matrixDataAynew = {
                   { -0.192452156653764,	0,	-0.0894047052968203, 0,	0.00147980804489297,	0.0253768160951873,	0,	0,	0}, 
                   { 4.07121618296722e-08,	-0.161419827182244,	-0.0872441643100858,	0.000214928268074650,	0.00126163290350044,	-2.55202854327981e-07,	0.000787385484194946,	0.00176331667727933,	8.82660973663266e-07}, 
                   { 0,	1,	-0.866746961954535,	0,	0.0143462150334103,	0,	0,	0,	0}, 
                   { 0,	0,	2.72791209869176,	-0.0179030730653470,	-0.0472208063434744,	0,	0,	0.0200000000000000,	0}, 
                   { -1.31706706760705e-07,	-0.340696883071355,	-21.3833287971200,	0.000261306506888773,	0.353704337655556,	3.73709775142230e-06,	0.00125203436138109,	0.00209372258711386,	-4.60839723777189e-05}, 
                   { -0.000935124787412196,	0.00593536058924734,	-0.682906655672276,	3.64208027029023e-07,	0.0113030537933972,	-0.00223363765895812,	1.78024017119901e-06,	2.66814864167283e-06,	4.39543472796001e-06}, 
                   { 0,	0,	-1.32146130343391,	0,	0.0260104999098095,	0,	-0.100000000000000,	-0.0400000000000000,	0}, 
                   { 0,	0,	0.722407343045617,	0,	-0.0119571357500652,	0,	0.0625000000000000,	0,	0}, 
                   { -2.84845995831803e-05,	19.3920788749716,	-123.580021675090,	-0.0148617536901368,	2.05789160684167,	0.00717150292638041,	-0.0566453333011438,	-0.120059008617330,	-0.0268711445991165}};
                this.A = new Array2DRowRealMatrix(matrixDataAynew);
                
                double[][] matrixDataBynew = {
                  {-0.0894047052968203}, 
                  {-0.0743545050672781}, 
                  {-0.866746961954535}, 
                  {2.85291209869176}, 
                  {-21.3649022499742}, 
                  {-0.682883528576174}, 
                  {-1.32146130343391}, 
                  {0.722407343045617}, 
                  {-124.554836468359}}; 
                this.B = new Array2DRowRealMatrix(matrixDataBynew);
        
                double[][] matrixDataCynew = {{-0.00163191332556388, 5993.65174359546, 475.680163363957, -7.92461801378850, -1.14854419377458, 0.692488171575920, -29.0428917445324, -65.0057019801426, 0.811593559483769}};
                this.C = new Array2DRowRealMatrix(matrixDataCynew);
        
                double[][] matrixDataDynew = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDynew);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

                case "newZ":
                double[][] matrixDataAznew = {
                    {-0.393619875940616, -1.30630403585788, -1.38277313419612e-05, 0.0270204031292620, 6.05268962591834e-08, 0.00298326497322976, 0.0120224821105337}, 
                    {1, -2.39478763527140, 0, 0.0525585161945854, 0, 0, 0}, 
                    {0, -2.63822807961569, -7.42978833360706, -0.0298868720034706, 0, 0, 0.640000000000000}, 
                    {-0.0109394951648171, -28.5504245634278, -1.49645945184921e-09, 0.626577683704759, -1.84043519124374e-06, 5.09225616146123e-05, 0.000104718297330254}, 
                    {0.00438889859510517, -1.95320766053907, 8.15633894612427e-12, 0.0428673463425254, -0.00900620452351860, -9.81953122540290e-07, -1.50267942005397e-06}, 
                    {0, -2.29009517255919, 0, 0.0557475875089904, 0, -0.100000000000000, -0.0400000000000000}, 
                    {0, 1.83018180075858, 0, -0.0401670855475683, 0, 0.0625000000000000, 0}};
                this.A = new Array2DRowRealMatrix(matrixDataAznew);
                
                double[][] matrixDataBznew = {
                   {-1.22883614473654}, 
                   {-2.39478763527140}, 
                   {1.36177192038431}, 
                   {-28.5496241609569}, 
                   {-1.95324545394022}, 
                   {-2.29009517255919}, 
                   {1.83018180075858}}; 
                this.B = new Array2DRowRealMatrix(matrixDataBznew);
        
                double[][] matrixDataCznew = {{4936.66187848822, 971.578947815358, 0.173423239918885, -0.640615032247597, 0.759310212895835, -37.4152103767097, -150.782348015456}};
                this.C = new Array2DRowRealMatrix(matrixDataCznew);
        
                double[][] matrixDataDznew = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDznew);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

            default:
                break;
        }
    }

    // Perform one-step integration
    public RealMatrix integrateOneStep(RealMatrix u, double dt) {
        // Compute state update: x(t + dt) = x(t) + dt * (Ax(t) + Bu(t))
        RealMatrix Ax = A.multiply(x);
        RealMatrix Bu = B.multiply(u);
        RealMatrix xNext = x.add(Ax.add(Bu).scalarMultiply(dt));
        x = xNext;
        // Compute output: y(t) = Cx(t) + Du(t)
        RealMatrix y = C.multiply(xNext).add(D.multiply(u));

        // Return the updated state (xNext) and output (y)
        return y;
    }
}

    // public static void initializeHinfityControllers() {
    //     // Example usage
    //     LinearSystemIntegration system = new LinearSystemIntegration();

    //     // Set initial state x0 and input u0
    //     RealMatrix x0 = new Array2DRowRealMatrix(/* Initial state */);
    //     RealMatrix u0 = new Array2DRowRealMatrix(/* Input */);

    //     // Perform one-step integration with a time step (dt)
    //     double dt = 0.01; // Set your desired time step
    //     RealMatrix yNext = system.integrateOneStep(x0, u0, dt);

    //     // Print the output
    //     System.out.println("Output at t + dt: " + yNext);
    // }


