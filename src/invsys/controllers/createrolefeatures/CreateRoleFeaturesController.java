package invsys.controllers.createrolefeatures;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import invsys.controllers.formvalidation.ValidateInputs;
import invsys.entities.RoleFunctions;
import invsys.entitiydao.RoleDao;
import invsys.entitiydao.impl.RoleDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class CreateRoleFeaturesController implements Initializable {
	
	@FXML
    private JFXTextField funcNameField;

    @FXML
    private JFXComboBox<String> parentFunCombo;
  
    @FXML
    private JFXComboBox<String> funcTypeCombo;
    
    
    
    //Dao handlers/Classes/Interface
    
    RoleDao roleDao = null;
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		roleDao = RoleDaoImpl.getDao();
		
		for (RoleFunctions x : roleDao.getRoleFeatures())	{
			parentFunCombo.getItems().add(x.getRoleFunction());
			
		}
		
		funcTypeCombo.getItems().addAll("Parent","Report");
		
		
//		funcTypeCombo.setOnAction(e-> {	
//			for (RoleFunctions x : roleDao.getParentRoleFeaturesByType(funcTypeCombo.getValue()))	{
//				parentFunCombo.getItems().add(x.getRoleFunction());
//				System.out.println(x.getRoleFunction());
//			}
//		});
		
	}
	
	
	  @FXML
	   private void clear() {
		  funcNameField.clear();
		  parentFunCombo.getSelectionModel().clearSelection();
	    }

	    @FXML
	    void createFunc(ActionEvent event) {
	    	RoleFunctions roleF = null;
	    	if(ValidateInputs.validateTextOnly(funcNameField , "Function Name Field")) {
	    		
	    		String featureName = funcNameField.getText();
	    		
	    		if(parentFunCombo.getValue() != null) {
	    			String comboText = parentFunCombo.getValue();
	    			roleF = roleDao.getRoleFeatureByName(comboText);
	    		}
	    		
	    		RoleFunctions newRoleF = new RoleFunctions();
	    		newRoleF.setRoleFunction(featureName);
	    		newRoleF.setMainRoleFunction(roleF);
	    		newRoleF.setRoleAcess(true);
	    		if(roleDao.createRoleFeatures(newRoleF)) {
	    			parentFunCombo.getItems().clear();
	    			for (RoleFunctions x : roleDao.getRoleFeatures())	{
	    				parentFunCombo.getItems().add(x.getRoleFunction());
	    			}
	    			System.out.println("Done");
	    			
	    			
	    		}
	    		clear();
	    	}

	    }

	    @FXML
	    void updateFunc(ActionEvent event) {
	    	
	    	

	    }
	    
	    @FXML
	    void deletFunc(ActionEvent event) {
	    	
	    	

	    }
	
	
}
