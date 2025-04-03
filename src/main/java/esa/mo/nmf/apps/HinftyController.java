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
                    {-1.86547690346604,	-1.86036005567003,	0.0221870079908689,	0.000288378358289908,	1.60982438481613e-07,	0.0122932473005856,	0.378575588164110},
                    {1,	-0.584976866328640,	0,	0.00139178048664554,	0,	0,	0},
                    {0,	3.58215115440883,	-0.265599452652648,	-0.0846572685179599,	0,	0,	6.96570347520000},
                    {-3.83195049975970e-06,	-3.97732677000875,	1.71997364421455e-10,	0.00943329637168053,	-3.15574864946840e-08,	1.30318768910342e-08,	4.16374872052300e-08},
                    {2.85181185590703e-05,	-12.4526036113597,	-3.62041679184763e-10,	0.0296273108650305,	-0.0265594280949905, -4.40517482415068e-08,	-1.17775383170638e-07},
                    {0,	-0.0684565349304879,	0,	0.000460272954074480,	0,	-0.0583200000000000,	-0.0272097792000000},
                    {0,	0.0270743339460013,	0,	-6.44154185300726e-05,	0,	0.0312500000000000,	0}};
                this.A = new Array2DRowRealMatrix(matrixDataAynew);
                
                double[][] matrixDataBynew = {
                    {-0.120358030722514},
                    {-0.584976866328640},
                    {35.5821511544088},
                    {-3.97732655077656},
                    {-12.4526043598912},
                    {-0.0684565349304879},
                    {0.0270743339460013}};
                this.B = new Array2DRowRealMatrix(matrixDataBynew);
        
                double[][] matrixDataCynew = {{68124.6109988273,	63542.4436863732,	-810.238543182974,	-0.0738335337900623,	35.1669422928205,	-448.932221411456,	-13825.0517223848}};
                this.C = new Array2DRowRealMatrix(matrixDataCynew);
        
                double[][] matrixDataDynew = {{0.0}};
                this.D = new Array2DRowRealMatrix(matrixDataDynew);

                this.x = new Array2DRowRealMatrix(A.getColumnDimension(),1);
                break;

                case "newZ":
                double[][] matrixDataAznew = {
                    {-3.99338619258351,	-8.43031810746166,	0.101288362769286,	0.00108872893065413,	1.57196715345684e-06,	0.0267681156000449,	1.73548680555847},
                    {1,	-0.998420871228267,	0,	0.00237544895536857,	0,	0,	0},
                    {0,	-20.3779177072992,	-0.909050690484603,	-0.103785921147382,	0,	0,	13.9314069504000},
                    {-3.27256166552135e-07,	-5.42963867394971,	5.96318507919538e-12,	0.0128886460240478,	-5.82015488891493e-09,	1.11343471914806e-09,	3.58879300322989e-09},
                    {0.000114026979953526,	-34.3219450241373,	-6.44047381147728e-09,	0.0816590010985656,	-0.0909032989777239,	-4.44458374229793e-07,	-2.06788987091175e-06},
                    {0,	-0.0431246523753985,	0,	0.000400003186397269,	0,	-0.0583200000000000,	-0.0272097792000000},
                    {0,	0.0204803011981856,	0,	-4.87268560672355e-05,	0,	0.0312500000000000,	0}};
                this.A = new Array2DRowRealMatrix(matrixDataAznew);
                
                double[][] matrixDataBznew = {
                    {-0.456751626631202},
                    {-0.998420871228267},
                    {43.6220822927008},
                    {-5.42963865507690},
                    {-34.3219551356560},
                    {-0.0431246523753985},
                    {0.0204803011981856}};
                this.B = new Array2DRowRealMatrix(matrixDataBznew);
        
                double[][] matrixDataCznew = {{42608.3332360482,	85075.7631019940,	-1080.71899527839,	-0.0215817304499789,	30.0791831537301,	-285.608437196980,	-18517.1672790707}};
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


