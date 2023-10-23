package com.byes.paap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;

import com.planonsoftware.platform.backend.businessrule.v3.IBusinessRule;
import com.planonsoftware.platform.backend.businessrule.v3.IBusinessRuleContext;
import com.planonsoftware.platform.backend.data.v1.IAssociation;
import com.planonsoftware.platform.backend.data.v1.IBusinessObject;
import com.planonsoftware.platform.backend.data.v1.IFieldDefinition;

public class SetContractMaintenanceActivityInverval implements IBusinessRule {

    @Override
    public void execute(IBusinessObject newBO, IBusinessObject oldBO, IBusinessRuleContext context) {

        IBusinessObject maintenanceRegime = newBO.getReferenceFieldByName("ActivityCategoryRef").getValue();
        IAssociation timeSchedules = newBO.getAssociationByName("BaseContractActivityDefinitionTimeSchedule", "ContractActivityDefinitionRef");

        Iterator<IBusinessObject> timeSchedulesIterator = timeSchedules.getIterator();

        while (timeSchedulesIterator.hasNext()) {
            IBusinessObject timeSchedule = timeSchedulesIterator.next();

            String boName = timeSchedule.getTypeName();

            String frequency = null;

            if ("ContractActivityDefinitionTimeScheduleFH".equals(boName)) {
                frequency = "H";
            } else if ("ContractActivityDefinitionTimeScheduleFD".equals(boName)) {
                frequency = "D";
            } else if ("ContractActivityDefinitionTimeScheduleFW".equals(boName)) {
                frequency = "W";
            } else if ("ContractActivityDefinitionTimeScheduleFMD".equals(boName) || "ContractActivityDefinitionTimeScheduleFMW".equals(boName)) {
                frequency = "M";
            } else if ("ContractActivityDefinitionTimeScheduleFY".equals(boName)) {
                frequency = "A";
            }
            int interval = timeSchedule.getIntegerFieldByName("Interval").getValue();
            
            if (frequency != null) {
                newBO.getBigDecimalFieldByName("FreeDecimal1").setValue(BigDecimal.valueOf(interval));
                newBO.getCodesCodeNameFieldByName("FreeString3").setValueAsString(frequency);
            }
        }


    }
}