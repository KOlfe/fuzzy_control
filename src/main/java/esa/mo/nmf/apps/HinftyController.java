package esa.mo.nmf.apps;

import org.hipparchus.linear.Array2DRowRealMatrix;
import org.hipparchus.linear.RealMatrix;

public class HinftyController {

    // Define system matrices A, B, C, and D
    private RealMatrix A; // State matrix
    private RealMatrix B; // Input matrix
    private RealMatrix C; // Output matrix
    private RealMatrix D; // Feedthrough matrix

    // Constructor to initialize matrices (you can set your own values)
    public HinftyController(RealMatrix A, RealMatrix B, RealMatrix C, RealMatrix D) {
        // Initialize A, B, C, and D matrices
        // Example: A = new Array2DRowRealMatrix(...);           
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
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
                    {0, -0.16669, 0, 0.0031545, 0. -0.05832, -0.02721},
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
                break;

                case "defaultYZ":
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
                break;
            default:
                break;
        }
    }

    // Perform one-step integration
    public RealMatrix integrateOneStep(RealMatrix x, RealMatrix u, double dt) {
        // Compute state update: x(t + dt) = x(t) + dt * (Ax(t) + Bu(t))
        RealMatrix Ax = A.multiply(x);
        RealMatrix Bu = B.multiply(u);
        RealMatrix xNext = x.add(Ax.add(Bu).scalarMultiply(dt));

        // Compute output: y(t) = Cx(t) + Du(t)
        RealMatrix y = C.multiply(xNext).add(D.multiply(u));

        // Return the updated state (xNext) and output (y)
        return y;
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
}
