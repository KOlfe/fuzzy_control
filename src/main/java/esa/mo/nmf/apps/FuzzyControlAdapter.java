/* ----------------------------------------------------------------------------
 * Copyright (C) 2015      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under the European Space Agency Public License, Version 2.0
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.nmf.apps;

import esa.mo.helpertools.connections.ConnectionConsumer;
import esa.mo.helpertools.helpers.HelperAttributes;
import esa.mo.nmf.MCRegistration;
import esa.mo.nmf.MCRegistration.RegistrationMode;
import esa.mo.nmf.MonitorAndControlNMFAdapter;
import esa.mo.nmf.NMFException;
import esa.mo.nmf.NMFInterface;
import static esa.mo.nmf.apps.FuzzyControl.connector;
import static esa.mo.platform.impl.util.HelperGPS.getDataFieldsFromBestXYZ;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ccsds.moims.mo.com.structures.ObjectId;
import org.ccsds.moims.mo.com.structures.ObjectIdList;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.provider.MALInteraction;
import org.ccsds.moims.mo.mal.structures.Attribute;
import org.ccsds.moims.mo.mal.structures.Duration;
import org.ccsds.moims.mo.mal.structures.FloatList;
import org.ccsds.moims.mo.mal.structures.Identifier;
import org.ccsds.moims.mo.mal.structures.IdentifierList;
import org.ccsds.moims.mo.mal.structures.Pair;
import org.ccsds.moims.mo.mal.structures.PairList;
import org.ccsds.moims.mo.mal.structures.UInteger;
import org.ccsds.moims.mo.mal.structures.UOctet;
import org.ccsds.moims.mo.mal.structures.UShort;
import org.ccsds.moims.mo.mal.structures.Union;
import org.ccsds.moims.mo.mal.structures.UpdateHeaderList;
import org.ccsds.moims.mo.mal.transport.MALMessageHeader;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetails;
import org.ccsds.moims.mo.mc.action.structures.ActionDefinitionDetailsList;
import org.ccsds.moims.mo.mc.conversion.structures.DiscreteConversionDetails;
import org.ccsds.moims.mo.mc.conversion.structures.DiscreteConversionDetailsList;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterConversion;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetails;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterDefinitionDetailsList;
import org.ccsds.moims.mo.mc.parameter.structures.ParameterRawValueList;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetails;
import org.ccsds.moims.mo.mc.structures.ArgumentDefinitionDetailsList;
import org.ccsds.moims.mo.mc.structures.AttributeValueList;
import org.ccsds.moims.mo.mc.structures.ConditionalConversion;
import org.ccsds.moims.mo.mc.structures.ConditionalConversionList;
import org.ccsds.moims.mo.mc.structures.ParameterExpression;
import org.ccsds.moims.mo.platform.autonomousadcs.consumer.AutonomousADCSAdapter;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.ActuatorsTelemetry;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeMode;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeBDot;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeNadirPointing;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeSingleSpinning;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeSunPointing;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeTargetTracking;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeTelemetry;
import org.ccsds.moims.mo.platform.autonomousadcs.structures.Quaternion;
import org.ccsds.moims.mo.platform.gps.body.GetLastKnownPositionAndVelocityResponse;
import org.ccsds.moims.mo.platform.structures.VectorF3D;
/*import org.ccsds.moims.mo.platform.gps.body.GetLastKnownPositionResponse;
import org.ccsds.moims.mo.platform.gps.consumer.GPSAdapter;
import org.ccsds.moims.mo.platform.gps.structures.SatelliteInfoList;
*/
/**
 * The adapter for the NMF App
 */
public class FuzzyControlAdapter extends MonitorAndControlNMFAdapter
{

  private static final String ACTION_NADIR_POINTING_MODE = "ADCS_NadirPointingMode";
  private static final String ACTION_SUN_POINTING_MODE = "ADCS_SunPointingMode";
  private static final String ACTION_UNSET = "ADCS_UnsetAttitude";
  private static final String ACTION_SET_TARGET_ATTITUDE = "ADCS_SetTargetAttitude";
  private static final String ACTION_VERIFY_CHECKSUM = "Verify_checksum";
  private static final String ACTION_EDIT_FCL = "Edit_fcl" ;
  private static final String ACTION_APPLY_FCL_CHANGES = "Apply_fcl_changes";
  private static final String ACTION_UPDATE_FILES = "Update_files";
  private static final String ACTION_RELEASE_SCRIPT = "Release_script";
  private static final String ACTION_STOP_SCRIPT = "Stop_script";
  private static final String ACTION_CHANGE_CONTROLLER = "Change_controller";
  private static final String ACTION_SET_PID_GAINS = "Set_PID_gains";
  private static final String ACTION_SET_iADCS_REFRESH_RATE = "Set_iADCS_RefreshRate";
  private static final String PARAMETER_ADCS_MODE = "ADCS_ModeOperation";
//  private static final String PARAMETER_ADCS_DURATION = "ADCS_RemainingControlDuration";
//  private static final String PARAMETER_ANGULAR_VELOCITY_X = "AngularVelocity_X";
//  private static final String PARAMETER_ANGULAR_VELOCITY_Y = "AngularVelocity_Y";
//  private static final String PARAMETER_ANGULAR_VELOCITY_Z = "AngularVelocity_Z";
  private static final String PARAMETER_ATTITUDE_Q_A = "AttitudeQuaternion_a";
  private static final String PARAMETER_ATTITUDE_Q_B = "AttitudeQuaternion_b";
  private static final String PARAMETER_ATTITUDE_Q_C = "AttitudeQuaternion_c";
  private static final String PARAMETER_ATTITUDE_Q_D = "AttitudeQuaternion_d";
  static final String PARAMETER_ATTITUDE_TARGET_Q_A = "AttitudeTargetQuaternion_a";
  static final String PARAMETER_ATTITUDE_TARGET_Q_B = "AttitudeTargetQuaternion_b"; 
  static final String PARAMETER_ATTITUDE_TARGET_Q_C = "AttitudeTargetQuaternion_c";
  static final String PARAMETER_ATTITUDE_TARGET_Q_D = "AttitudeTargetQuaternion_d";
  static final String PARAMETER_CONTROLLER_TYPE = "Controller_Type"; 
//  private static final String PARAMETER_MTQ_X = "MagnetorquerMoment_X";
//  private static final String PARAMETER_MTQ_Y = "MagnetorquerMoment_Y";
//  private static final String PARAMETER_MTQ_Z = "MagnetorquerMoment_Z";
//  private static final String PARAMETER_MAG_X = "MagneticField_X";
//  private static final String PARAMETER_MAG_Y = "MagneticField_Y";
//  private static final String PARAMETER_MAG_Z = "MagneticField_Z";
  private static final String TM_PACKET = "TelemetryPacket";
  private static final String PARAMETER_FILE_ID ="fileID";
  private static final String PARAMETER_MD5 = "MD5";
  private static UOctet attitude_operational_mode;
  private static final Duration TM_PACKET_SENDING_INTERVAL = new Duration(2.00);
  private static final Duration ATTITUDE_MONITORING_INTERVAL = new Duration(2.00);
  private static final Logger LOGGER = Logger.getLogger(FuzzyControlAdapter.class.getName());
  private NMFInterface nmf;
  
  private static Quaternion InertialAttitude = new Quaternion(
          Float.parseFloat("1.0"),
          Float.parseFloat("0.0"),
          Float.parseFloat("0.0"),
          Float.parseFloat("0.0")
  ); 
  static VectorF3D mtqDipoleMoment = new VectorF3D();
  static FloatList wheelsSpeed = new FloatList(6);
  
  public FuzzyControlAdapter(final NMFInterface nmfProvider)
  {
    this.nmf = nmfProvider;
    for (int i=0;i<3;i++){
        wheelsSpeed.add(i,(float)0.0);
    }
//    System.out.println("initial size of wheelsSpeed: "+wheelsSpeed.size());
  }
  
  
  
  public static  Quaternion getInertialAttitude(){
      return InertialAttitude;
  }
  
  static void setInertialAttitude(Quaternion attitude){
      InertialAttitude = attitude;
  }
  
  
  private static enum AttitudeModeEnum
  {
    IDLE, BDOT, SUNPOINTING, SINGLESPINNING, TARGETTRACKING, NADIRPOINTING
  }

  private UOctet attitudeModeToParamValue(AttitudeMode attitude)
  {
    AttitudeModeEnum modeEnum;
    if (attitude == null) {
      modeEnum = AttitudeModeEnum.IDLE;
    } else if (attitude instanceof AttitudeModeBDot) {
      modeEnum = AttitudeModeEnum.BDOT;
    } else if (attitude instanceof AttitudeModeSunPointing) {
      modeEnum = AttitudeModeEnum.SUNPOINTING;
    } else if (attitude instanceof AttitudeModeSingleSpinning) {
      modeEnum = AttitudeModeEnum.SINGLESPINNING;
    } else if (attitude instanceof AttitudeModeTargetTracking) {
      modeEnum = AttitudeModeEnum.TARGETTRACKING;
    } else if (attitude instanceof AttitudeModeNadirPointing) {
      modeEnum = AttitudeModeEnum.NADIRPOINTING;
    } else {
      throw new IllegalArgumentException("Unrecognized attitude mode type!");
    }
    return new UOctet((short) modeEnum.ordinal());
  }

  @Override
  public void initialRegistrations(MCRegistration registration)
  { 
    registration.setMode(RegistrationMode.UPDATE_IF_EXISTS);

    // ===================================================================
    PairList mappings = new PairList();
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.IDLE.ordinal()), new Union("IDLE")));
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.BDOT.ordinal()), new Union("BDOT")));
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.SUNPOINTING.ordinal()), new Union(
        "SUNPOINTING")));
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.SINGLESPINNING.ordinal()), new Union(
        "SINGLESPINNING")));
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.TARGETTRACKING.ordinal()), new Union(
        "TARGETTRACKING")));
    mappings.add(new Pair(new UOctet((short) AttitudeModeEnum.NADIRPOINTING.ordinal()), new Union(
        "NADIRPOINTING")));

    DiscreteConversionDetailsList conversions = new DiscreteConversionDetailsList();
    conversions.add(new DiscreteConversionDetails(mappings));
    ParameterConversion paramConversion = null;

    try {
      ObjectIdList objIds = registration.registerConversions(conversions);

      if (objIds.size() == 1) {
        ObjectId objId = objIds.get(0);
        ParameterExpression paramExpr = null;

        ConditionalConversion condition = new ConditionalConversion(paramExpr, objId.getKey());
        ConditionalConversionList conditionalConversions = new ConditionalConversionList();
        conditionalConversions.add(condition);

        Byte convertedType = Attribute.STRING_TYPE_SHORT_FORM.byteValue();
        String convertedUnit = "n/a";

        paramConversion = new ParameterConversion(convertedType, convertedUnit,
            conditionalConversions);
      }
    } catch (NMFException | MALException | MALInteractionException ex) {
      Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE,
          "Failed to register conversion.", ex);
    }

    // ------------------ Parameters ------------------
    ParameterDefinitionDetailsList defsOther = new ParameterDefinitionDetailsList();
    IdentifierList paramOtherNames = new IdentifierList();

    defsOther.add(new ParameterDefinitionDetails(
        "The ADCS mode of operation",
        Union.UOCTET_SHORT_FORM.byteValue(),
        "",
        true,
        new Duration(0),
        null,
        paramConversion
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ADCS_MODE));
    
    defsOther.add(new ParameterDefinitionDetails(
        "All the telemetry fields are OK",
            Union.BOOLEAN_TYPE_SHORT_FORM.byteValue(),
            "",
            true,
            TM_PACKET_SENDING_INTERVAL,
            null,
            null
    ));
    paramOtherNames.add(new Identifier(TM_PACKET));    
        
    
    defsOther.add(new ParameterDefinitionDetails(
        "Scalar part of the attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_Q_A));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial X part of the attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_Q_B));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial Y part of the attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_Q_C));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial Z part of the attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_Q_D));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Scalar part of the commanded attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_TARGET_Q_A));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial X part of the commanded attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_TARGET_Q_B));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial Y part of the commanded attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_TARGET_Q_C));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Vectorial Z part of the commanded attitude quaternion in orbital frame",
            Union.FLOAT_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_ATTITUDE_TARGET_Q_D));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Selected file",
            Union.STRING_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_FILE_ID));
    
    defsOther.add(new ParameterDefinitionDetails(
        "MD5 checksum of selected file",
            Union.STRING_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_MD5));
    
    defsOther.add(new ParameterDefinitionDetails(
        "Controller in use: PID or Fuzzy",
            Union.STRING_SHORT_FORM.byteValue(),
            "",
            true,
            new Duration(0),
            null,
            null
    ));
    paramOtherNames.add(new Identifier(PARAMETER_CONTROLLER_TYPE));

    registration.registerParameters(paramOtherNames, defsOther);
    
    // ------------------ Aggregations ------------------
    
    // ------------------ Actions ------------------
    ActionDefinitionDetailsList actionDefs = new ActionDefinitionDetailsList();
    IdentifierList actionNames = new IdentifierList();

    ArgumentDefinitionDetailsList arguments1 = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Attribute._DURATION_TYPE_SHORT_FORM;
      String rawUnit = "seconds";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments1.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    
    ArgumentDefinitionDetailsList arguments_attitude = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_attitude.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_attitude.add(new ArgumentDefinitionDetails(new Identifier("1"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_attitude.add(new ArgumentDefinitionDetails(new Identifier("2"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_attitude.add(new ArgumentDefinitionDetails(new Identifier("3"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    ArgumentDefinitionDetailsList argument_checksum = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Attribute._STRING_TYPE_SHORT_FORM;
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      argument_checksum.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
     ArgumentDefinitionDetailsList arguments_fclEditor = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Attribute._STRING_TYPE_SHORT_FORM;
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_fclEditor.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Attribute._STRING_TYPE_SHORT_FORM;
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_fclEditor.add(new ArgumentDefinitionDetails(new Identifier("1"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Attribute._INTEGER_TYPE_SHORT_FORM;
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_fclEditor.add(new ArgumentDefinitionDetails(new Identifier("2"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Attribute._STRING_TYPE_SHORT_FORM;
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_fclEditor.add(new ArgumentDefinitionDetails(new Identifier("3"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    ArgumentDefinitionDetailsList arguments_PIDGains = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_PIDGains.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_PIDGains.add(new ArgumentDefinitionDetails(new Identifier("1"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    {
      Byte rawType = Union.FLOAT_SHORT_FORM.byteValue();
      String rawUnit = "";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_PIDGains.add(new ArgumentDefinitionDetails(new Identifier("2"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    ArgumentDefinitionDetailsList arguments_iADCSRefreshRate = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Attribute._INTEGER_TYPE_SHORT_FORM;
      String rawUnit = "milliseconds";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments_iADCSRefreshRate.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    
    /*
    ArgumentDefinitionDetailsList arguments_att_control = new ArgumentDefinitionDetailsList();
    {
      Byte rawType = Attribute._DURATION_TYPE_SHORT_FORM;
      String rawUnit = "seconds";
      ConditionalConversionList conditionalConversions = null;
      Byte convertedType = null;
      String convertedUnit = null;

      arguments1.add(new ArgumentDefinitionDetails(new Identifier("0"), null,
          rawType, rawUnit, conditionalConversions, convertedType, convertedUnit));
    }
    */
    

    ActionDefinitionDetails actionDef1 = new ActionDefinitionDetails(
        "Changes the spacecraft's attitude to sun pointing mode.",
        new UOctet((short) 0),
        new UShort(0),
        arguments1
    );
    actionNames.add(new Identifier(ACTION_SUN_POINTING_MODE));

    ActionDefinitionDetails actionDef2 = new ActionDefinitionDetails(
        "Changes the spacecraft's attitude to nadir pointing mode.",
        new UOctet((short) 0),
        new UShort(0),
        arguments1
    );
    actionNames.add(new Identifier(ACTION_NADIR_POINTING_MODE));

    ActionDefinitionDetails actionDef3 = new ActionDefinitionDetails(
        "Unsets the spacecraft's attitude.",
        new UOctet((short) 0),
        new UShort(0),
        new ArgumentDefinitionDetailsList()
    );
    actionNames.add(new Identifier(ACTION_UNSET));
    
    ActionDefinitionDetails set_target_attitude = new ActionDefinitionDetails(
        "Set quaternion target values",
        new UOctet((short) 0),
        new UShort(0),
        arguments_attitude
    );
    actionNames.add(new Identifier(ACTION_SET_TARGET_ATTITUDE));
    
    ActionDefinitionDetails verify_checksum = new ActionDefinitionDetails(
        "Calculate MD5 checksum of a file",
        new UOctet((short) 0),
        new UShort(0),
        argument_checksum
    );
    actionNames.add(new Identifier(ACTION_VERIFY_CHECKSUM));
    
    ActionDefinitionDetails edit_fcl = new ActionDefinitionDetails(
        "Edit a fcl file",
        new UOctet((short) 0),
        new UShort(0),
        arguments_fclEditor
    );
    actionNames.add(new Identifier(ACTION_EDIT_FCL));
    
    ActionDefinitionDetails apply_fcl_changes = new ActionDefinitionDetails(
        "Apply fcl changes",
        new UOctet((short) 0),
        new UShort(0),
        argument_checksum
    );
    actionNames.add(new Identifier(ACTION_APPLY_FCL_CHANGES));
    
    ActionDefinitionDetails update_files = new ActionDefinitionDetails(
        "Apply fcl changes",
        new UOctet((short) 0),
        new UShort(0),
        new ArgumentDefinitionDetailsList()
    );
    actionNames.add(new Identifier(ACTION_UPDATE_FILES));
    
    ActionDefinitionDetails release_script = new ActionDefinitionDetails(
        "Apply fcl changes",
        new UOctet((short) 0),
        new UShort(0),
        new ArgumentDefinitionDetailsList()
    );
    actionNames.add(new Identifier(ACTION_RELEASE_SCRIPT));
    
    ActionDefinitionDetails stop_script = new ActionDefinitionDetails(
        "Stop timeline execution",
        new UOctet((short) 0),
        new UShort(0),
        new ArgumentDefinitionDetailsList()
    );
    actionNames.add(new Identifier(ACTION_STOP_SCRIPT));
    
    ActionDefinitionDetails change_controller = new ActionDefinitionDetails(
        "Change the control algorithm",
        new UOctet((short) 0),
        new UShort(0),
        argument_checksum
    );
    actionNames.add(new Identifier(ACTION_CHANGE_CONTROLLER));
    
    ActionDefinitionDetails set_PID_Gains = new ActionDefinitionDetails(
        "Set the proportional, integral and derivative gains",
        new UOctet((short) 0),
        new UShort(0),
        arguments_PIDGains
    );
    actionNames.add(new Identifier(ACTION_SET_PID_GAINS));
    
    ActionDefinitionDetails set_iADCS_RefreshRate = new ActionDefinitionDetails(
        "Set the I2C polling rate",
        new UOctet((short) 0),
        new UShort(0),
        arguments_iADCSRefreshRate  
    );
    actionNames.add(new Identifier(ACTION_SET_iADCS_REFRESH_RATE));

    actionDefs.add(actionDef1);
    actionDefs.add(actionDef2);
    actionDefs.add(actionDef3);
    actionDefs.add(set_target_attitude);
    actionDefs.add(verify_checksum);
    actionDefs.add(edit_fcl);
    actionDefs.add(apply_fcl_changes);
    actionDefs.add(update_files);
    actionDefs.add(release_script);
    actionDefs.add(stop_script);
    actionDefs.add(change_controller);
    actionDefs.add(set_PID_Gains);
    actionDefs.add(set_iADCS_RefreshRate);
    registration.registerActions(actionNames, actionDefs);
  }

  @Override
  public Attribute onGetValue(Identifier identifier, Byte rawType) throws IOException
  {
      if (nmf == null) {
      return null;
    }
      if (identifier == null) {
      LOGGER.log(Level.SEVERE,
          "The identifier object is null! Something is wrong!");
      return null;
    }
      if (TM_PACKET.equals(identifier.getValue()) ){
//          try {
              PrintWriter telemetryWriter = new PrintWriter(new FileWriter(FuzzyControl.TELEMETRY_FILE, true));
              /*System.out.println(nmf.getPlatformServices().getGPSService().getLastKnownPosition().getBodyElement0().toString());
              GetLastKnownPositionAndVelocityResponse lastKnownPositionAndVelocity = nmf.getPlatformServices().getGPSService().getLastKnownPositionAndVelocity();
              System.out.println(lastKnownPositionAndVelocity);
              getDataFieldsFromBestXYZ("a");*/
              //nmf.pushParameterValue("Time", System.currentTimeMillis()/1000L);
              
              telemetryWriter.print(System.currentTimeMillis()/1000L);              
              
              Quaternion inertialAttitude = getInertialAttitude();
              telemetryWriter.format("%13.8f", inertialAttitude.getA());
              telemetryWriter.format("%13.8f", inertialAttitude.getB());
              telemetryWriter.format("%13.8f", inertialAttitude.getC());
              telemetryWriter.format("%13.8f", inertialAttitude.getD());
              
              Quaternion currentOrbitalAttitude = OrbitalFrame.getOrbitalAttitude();
//              nmf.pushParameterValue(PARAMETER_ATTITUDE_Q_A, currentOrbitalAttitude.getA());
//              nmf.pushParameterValue(PARAMETER_ATTITUDE_Q_B, currentOrbitalAttitude.getB());
//              nmf.pushParameterValue(PARAMETER_ATTITUDE_Q_C, currentOrbitalAttitude.getC());
//              nmf.pushParameterValue(PARAMETER_ATTITUDE_Q_D, currentOrbitalAttitude.getD());
              
              telemetryWriter.format("%13.8f", currentOrbitalAttitude.getA());
              telemetryWriter.format("%13.8f", currentOrbitalAttitude.getB());
              telemetryWriter.format("%13.8f", currentOrbitalAttitude.getC());
              telemetryWriter.format("%13.8f", currentOrbitalAttitude.getD());
              
//              nmf.pushParameterValue(PARAMETER_MAG_X, FuzzyControl.magneticField.getX());
//              nmf.pushParameterValue(PARAMETER_MAG_Y, FuzzyControl.magneticField.getY());
//              nmf.pushParameterValue(PARAMETER_MAG_Z, FuzzyControl.magneticField.getZ());
              
              telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getX());
              telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getY());
              telemetryWriter.format("%16.7f", FuzzyControl.magneticField.getZ()); 

//              nmf.pushParameterValue(PARAMETER_ANGULAR_VELOCITY_X, FuzzyControl.angularVelocity.getX());
//              nmf.pushParameterValue(PARAMETER_ANGULAR_VELOCITY_Y, FuzzyControl.angularVelocity.getY());
//              nmf.pushParameterValue(PARAMETER_ANGULAR_VELOCITY_Z, FuzzyControl.angularVelocity.getZ());
              
              telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getX());
              telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getY());
              telemetryWriter.format("%16.7f", FuzzyControl.angularVelocity.getZ());
              
//              nmf.pushParameterValue(PARAMETER_MTQ_X, mtqDipoleMoment.getX());
//              nmf.pushParameterValue(PARAMETER_MTQ_Y, mtqDipoleMoment.getY());
//              nmf.pushParameterValue(PARAMETER_MTQ_Z, mtqDipoleMoment.getZ());
              
              telemetryWriter.format("%16.7f", mtqDipoleMoment.getX());
              telemetryWriter.format("%16.7f", mtqDipoleMoment.getY());
              telemetryWriter.format("%16.7f", mtqDipoleMoment.getZ());

//              nmf.pushParameterValue("WheelSpeed_X", wheelsSpeed.get(0));
//              nmf.pushParameterValue("WheelSpeed_Y", wheelsSpeed.get(1));
//              nmf.pushParameterValue("WheelSpeed_Z", wheelsSpeed.get(2));
              
              telemetryWriter.format("%16.7f", wheelsSpeed.get(0));
              telemetryWriter.format("%16.7f", wheelsSpeed.get(1));
              telemetryWriter.format("%16.7f", wheelsSpeed.get(2));
              
              //nmf.pushParameterValue("Controller_Type", FuzzyControl.controller_type);
              
//              nmf.pushParameterValue("Output_Z", FuzzyControl.actuation.getZ());
//              nmf.pushParameterValue("Output_X", FuzzyControl.actuation.getX());
//              nmf.pushParameterValue("Output_Y", FuzzyControl.actuation.getY());
              
              telemetryWriter.format("%16.7f", FuzzyControl.actuation.getX());
              telemetryWriter.format("%16.7f", FuzzyControl.actuation.getY());
              telemetryWriter.format("%16.7f", FuzzyControl.actuation.getZ());
              
//              nmf.pushParameterValue("error_X", FuzzyControl.error.getB());
//              nmf.pushParameterValue("error_Y", FuzzyControl.error.getC());
//              nmf.pushParameterValue("error_Z", FuzzyControl.error.getD());
              
              telemetryWriter.format("%13.8f", FuzzyControl.error.getB());
              telemetryWriter.format("%13.8f", FuzzyControl.error.getC());
              telemetryWriter.format("%13.8f", FuzzyControl.error.getD());
              telemetryWriter.println();
              
              telemetryWriter.close();
                            
              
              return (Attribute) HelperAttributes.javaType2Attribute(true);
//          } catch (NMFException ex) {
//              Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex);
//          }
          
      } else if (identifier.getValue().equals(PARAMETER_ADCS_MODE)){
          return attitude_operational_mode; 
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_Q_A)){
          return new Union(OrbitalFrame.getOrbitalAttitude().getA());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_Q_B)){
          return new Union(OrbitalFrame.getOrbitalAttitude().getB());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_Q_C)){
          return new Union(OrbitalFrame.getOrbitalAttitude().getC());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_Q_D)){
          return new Union(OrbitalFrame.getOrbitalAttitude().getD());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_TARGET_Q_A)){
          return new Union(FuzzyControl.getTargetAttitude().getA());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_TARGET_Q_B)){
          return new Union(FuzzyControl.getTargetAttitude().getB());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_TARGET_Q_C)){
          return new Union(FuzzyControl.getTargetAttitude().getC());
      } else if (identifier.getValue().equals(PARAMETER_ATTITUDE_TARGET_Q_D)){
          return new Union(FuzzyControl.getTargetAttitude().getD());
      } else if (identifier.getValue().equals(PARAMETER_FILE_ID)){
          return new Union(Util.FileID);
      } else if (identifier.getValue().equals(PARAMETER_MD5)){
          return new Union(Util.MD5);
      } else if (identifier.getValue().equals(PARAMETER_CONTROLLER_TYPE)){
          return new Union(FuzzyControl.getControllerType());
      }
    return null;
  }

  @Override
  public Boolean onSetValue(IdentifierList identifiers, ParameterRawValueList values)
  {
    return false; // Parameter has not been set
  }

  @Override
  public boolean isReadOnly(Identifier name)
  {
    return true; // No parameter is directly writable
  }

  /**
   * The user must implement this interface in order to link a certain action Identifier to the
   * method on the application
   *
   * @param name                Name of the Parameter
   * @param attributeValues
   * @param actionInstanceObjId
   * @param reportProgress      Determines if it is necessary to report the execution
   * @param interaction         The interaction object progress of the action
   *
   * @return Returns null if the Action was successful. If not null, then the returned value should
   * hold the error number
   */
  @Override
  public UInteger actionArrived(Identifier name, AttributeValueList attributeValues,
      Long actionInstanceObjId, boolean reportProgress, MALInteraction interaction)
  {
    if (nmf == null) {
      return new UInteger(0);
    }

   /* LOGGER.log(Level.INFO, "Action {0} with parameters '{'{1}'}' arrived.",
        new Object[]{name.toString(),
          attributeValues.stream().map(HelperAttributes::attribute2string)
              .collect(Collectors.joining(", "))});
*/
    // Action dispatcher
    if (null != name.getValue()) {
//        System.out.println(name.getValue());
      switch (name.getValue()) {
        case ACTION_SUN_POINTING_MODE:
            executeAdcsModeAction((Duration) attributeValues.get(0).getValue(),
              new AttitudeModeSunPointing());
            try {
              Thread.sleep(2000);  
              nmf.pushParameterValue(PARAMETER_ADCS_MODE, attitude_operational_mode);
            } catch (NMFException ex) {
              LOGGER.log(Level.SEVERE, "Error when propagating active ADCS mode", ex);
            } catch (InterruptedException ex) {
              LOGGER.log(Level.SEVERE, null, ex);
      }
               break;
        case ACTION_NADIR_POINTING_MODE:
            executeAdcsModeAction((Duration) attributeValues.get(0).getValue(),
              new AttitudeModeNadirPointing());
            try {
              Thread.sleep(2000); 
              nmf.pushParameterValue(PARAMETER_ADCS_MODE, attitude_operational_mode);
            } catch (NMFException ex) {
              LOGGER.log(Level.SEVERE, "Error when propagating active ADCS mode", ex);
            } catch (InterruptedException ex) {
              LOGGER.log(Level.SEVERE, null, ex);
      }
               break;
        case ACTION_UNSET:
          executeAdcsModeAction(null, null);
          try {
              Thread.sleep(2000); 
              nmf.pushParameterValue(PARAMETER_ADCS_MODE, attitude_operational_mode);
            } catch (NMFException ex) {
              LOGGER.log(Level.SEVERE, "Error when propagating active ADCS mode", ex);
            } catch (InterruptedException ex) {
              LOGGER.log(Level.SEVERE, null, ex);
      }
               break;
        case ACTION_SET_TARGET_ATTITUDE:
            
            FuzzyControl.setTargetAttitude(
                Float.parseFloat(attributeValues.get(0).getValue().toString()),
                Float.parseFloat(attributeValues.get(1).getValue().toString()),
                Float.parseFloat(attributeValues.get(2).getValue().toString()),
                Float.parseFloat(attributeValues.get(3).getValue().toString()));
            
                   
            break;
        case ACTION_VERIFY_CHECKSUM:
            try {
                String checksum = Util.calculate_md5(attributeValues.get(0).getValue().toString());
                nmf.pushParameterValue(PARAMETER_FILE_ID,attributeValues.get(0).getValue().toString());
                nmf.pushParameterValue(PARAMETER_MD5,checksum);
//                System.out.println("MD5 :"+ checksum);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NMFException ex) {
                Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            break;
        case ACTION_EDIT_FCL:
//            System.out.println(attributeValues.get(0).getValue().toString()+" "+ 
//                                attributeValues.get(1).getValue().toString()+" "+ 
//                                attributeValues.get(2).getValue().toString()+" with "+
//                                attributeValues.get(3).getValue().toString());
            FCLEditor.fclEditor(attributeValues.get(0).getValue().toString(), 
                                attributeValues.get(1).getValue().toString(), 
                                Integer.parseInt(attributeValues.get(2).getValue().toString()),
                                attributeValues.get(3).getValue().toString());
            break;
        case ACTION_APPLY_FCL_CHANGES:
//            System.out.println("cambio el controlador");
            FuzzyControl.initiateFIS(attributeValues.get(0).getValue().toString());
            break;
        case ACTION_UPDATE_FILES:
            FuzzyControl.checkFilesUpdates();
            break;
        case ACTION_RELEASE_SCRIPT:
            FuzzyControl.TIMELINE_EXECUTOR = new ScriptExecutor("timeline.js");
            FuzzyControl.TIMELINE_EXECUTOR.start();
            break;
        case ACTION_STOP_SCRIPT:
            FuzzyControl.stopTimeline();
            break;
        case ACTION_CHANGE_CONTROLLER:
            FuzzyControl.setControllerType(attributeValues.get(0).getValue().toString());
            break;
        case ACTION_SET_PID_GAINS:
            PIDComparator.setPIDGains(Float.parseFloat(attributeValues.get(0).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(1).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(2).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(3).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(4).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(5).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(6).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(7).getValue().toString()),
                                           Float.parseFloat(attributeValues.get(8).getValue().toString()));
            break;
        case ACTION_SET_iADCS_REFRESH_RATE:
            FuzzyControl.setiADCSRefreshRate(Integer.parseInt(attributeValues.get(0).getValue().toString()));
        default:
          break;
      }
    }
    return null; // Action successful
  }

  private UInteger executeAdcsModeAction(Duration duration, AttitudeMode attitudeMode)
  {
    if (duration != null)
    {
      // Negative Durations are not allowed!
      if (duration.getValue() < 0) {
        return new UInteger(1);
      }
      if (duration.getValue() == 0) {
        // Adhere to the ADCS Service interface
        duration = null;
      }
    }
    try {
      nmf.getPlatformServices().getAutonomousADCSService().setDesiredAttitude(
          duration, attitudeMode);
    } catch (MALInteractionException | MALException | NMFException ex) {
      LOGGER.log(Level.SEVERE, null, ex);
      return new UInteger(3);
    } catch (IOException ex) {
      LOGGER.log(Level.SEVERE, null, ex);
      return new UInteger(4);
    }
    return null; // Success
  }

  public void startAdcsAttitudeMonitoring()
  {
    try {
      // Subscribe monitorAttitude
      nmf.getPlatformServices().getAutonomousADCSService().monitorAttitudeRegister(
          ConnectionConsumer.subscriptionWildcard(),
          new DataReceivedAdapter()
      );
      nmf.getPlatformServices().getAutonomousADCSService().enableMonitoring(true,
          ATTITUDE_MONITORING_INTERVAL);
    } catch (IOException | MALInteractionException | MALException | NMFException ex) {
      LOGGER.log(Level.SEVERE, "Error when setting up attitude monitoring.", ex);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException ex1) {
//            Logger.getLogger(FuzzyControlAdapter.class.getName()).log(Level.SEVERE, null, ex1);
//        }
//      FuzzyControl.tryReconection();
    }
  }

  public class DataReceivedAdapter extends AutonomousADCSAdapter
  {

    @Override
    public void monitorAttitudeNotifyReceived(
        final MALMessageHeader msgHeader,
        final Identifier lIdentifier, final UpdateHeaderList lUpdateHeaderList,
        org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeTelemetryList attitudeTelemetryList,
        org.ccsds.moims.mo.platform.autonomousadcs.structures.ActuatorsTelemetryList actuatorsTelemetryList,
        org.ccsds.moims.mo.mal.structures.DurationList controlDurationList,
        org.ccsds.moims.mo.platform.autonomousadcs.structures.AttitudeModeList attitudeModeList,
        final Map qosp)
    {
      for (AttitudeTelemetry attitudeTm : attitudeTelemetryList) {
//          try {
              OrbitalFrame.nmfSunVector = attitudeTm.getSunVector();
//          attMode = AttitudeMode.NADIRPOINTING;
FuzzyControl.magneticField = attitudeTm.getMagneticField();
FuzzyControl.angularVelocity = attitudeTm.getAngularVelocity();
setInertialAttitude(attitudeTm.getAttitude());
/*
nmf.pushParameterValue(PARAMETER_SUN_VECTOR_X, sunVector.getX());
nmf.pushParameterValue(PARAMETER_SUN_VECTOR_Y, sunVector.getY());
nmf.pushParameterValue(PARAMETER_SUN_VECTOR_Z, sunVector.getZ());
*/

//LOGGER.log(Level.INFO, String.valueOf(System.currentTimeMillis()/1000L));
//nmf.pushParameterValue("PARAMETER_ATTITUDE_Q_A",  FuzzyControl.currentAttitude.getA());
//nmf.pushParameterValue("PARAMETER_ATTITUDE_Q_B",  FuzzyControl.currentAttitude.getB());
//nmf.pushParameterValue("PARAMETER_ATTITUDE_Q_C",  FuzzyControl.currentAttitude.getC());
//nmf.pushParameterValue("PARAMETER_ATTITUDE_Q_D",  FuzzyControl.currentAttitude.getD());
//          } catch (NMFException ex) {
//              LOGGER.log(Level.SEVERE, null, ex);
//          }

      }
      for (ActuatorsTelemetry actuatorsTm : actuatorsTelemetryList) {
          mtqDipoleMoment = actuatorsTm.getMtqDipoleMoment();
          wheelsSpeed = actuatorsTm.getCurrentWheelSpeed().getRotationalSpeed();
          //System.out.println("update wheel speeds: "+wheelsSpeed.size());
          //Vector3D angularVelocity = attitudeTm.getAngularVelocity();
          //Quaternion attitude = attitudeTm.getAttitude();
      }
      for (Object activeAttitudeMode : attitudeModeList) {
          attitude_operational_mode = attitudeModeToParamValue(
              (AttitudeMode) activeAttitudeMode);
//        try {
//          nmf.pushParameterValue(PARAMETER_ADCS_MODE, attitudeModeToParamValue(
//              (AttitudeMode) activeAttitudeMode));
//        } catch (NMFException ex) {
//          LOGGER.log(Level.SEVERE, "Error when propagating active ADCS mode", ex);
//        }
      }
//      for (Duration remainingDuration : controlDurationList) {
//        try {
//          if (remainingDuration != null) {
//            nmf.pushParameterValue(PARAMETER_ADCS_DURATION, remainingDuration);
//          } else {
//            nmf.pushParameterValue(PARAMETER_ADCS_DURATION, new Duration(0));
//          }
//        } catch (NMFException ex) {
//          LOGGER.log(Level.SEVERE, "Error when propagating active ADCS mode duration", ex);
//        }
//      }
    }
  }
}
