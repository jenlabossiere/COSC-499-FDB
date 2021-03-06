package Prescriber;

import Info.Drug;
import Info.DrugInteraction;
import Info.Patient;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class DrugInteractionTest {
    private FdbPrescriberUnoptimized fdbPrescriberUnoptimized;

    //Requests a connection to the database
    @BeforeClass
    public void init() throws Exception {
        fdbPrescriberUnoptimized = new FdbPrescriberUnoptimized();
    }

    //closes connection
    @AfterClass
    public void end() throws Exception {
        fdbPrescriberUnoptimized.closePrescriber();
    }

    @Test
    public void testSizeOfQueryDrugToDrugInteractionsWithOnlyOneReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("PRENATAL/POSTPARTUM VIT/MIN");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("CARDIOQUIN 275MG TABLET");
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0),patient);
        Assert.assertEquals(queryDrugToDrugInteractionResult.size(), 1);
    }

    @Test
    public void testNameOfQueryingDrugToDrugInteractionWithOnlyOneReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("PRENATAL/POSTPARTUM VIT/MIN");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("CARDIOQUIN 275MG TABLET");
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0), patient);
        String drugToDrugClinicalEffectTextResult = queryDrugToDrugInteractionResult.get(0).getInteractionDescription();
        Assert.assertEquals(drugToDrugClinicalEffectTextResult, "PRENATAL/POSTPARTUM VIT/MIN Mixed effects of the latter drug CARDIOQUIN 275MG TABLET");
    }

    @Test
    public void testSizeOfQueryDrugToDrugInteractionsWithZeroReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("ADDERALL XR 10 MG CAPSULE");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("TYLENOL WITH CODEINE ELIXIR");
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0),patient);
        Assert.assertEquals(queryDrugToDrugInteractionResult.size(), 0);
    }

    @Test
    public void testNameOfQueryingDrugToDrugInteractionWithZeroReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("ADDERALL XR 10 MG CAPSULE");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("TYLENOL WITH CODEINE ELIXIR");
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0), patient);
        String drugToDrugClinicalEffectTextResult = null;
        if(!queryDrugToDrugInteractionResult.isEmpty()){
            drugToDrugClinicalEffectTextResult = queryDrugToDrugInteractionResult.get(0).getInteractionDescription();
        }
        Assert.assertEquals(drugToDrugClinicalEffectTextResult, null);
    }

    @Test
    public void testSizeOfQueryDrugToDrugInteractionsWithManyReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("PRENATAL/POSTPARTUM VIT/MIN");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("APO-QUIN-G 325 MG TABLET");
        queryCurrentDrugResult.add(fdbPrescriberUnoptimized.queryDrugs("BIO BALANCED CALC/MAG TAB").get(0));
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        patient.addDrug(queryCurrentDrugResult.get(1));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0),patient);
        Assert.assertEquals(queryDrugToDrugInteractionResult.size(), 3);
    }

    @Test
    public void testNameOfQueryDrugToDrugInteractionsWithManyReturned() {
        List<Drug> queryNewDrugResult = fdbPrescriberUnoptimized.queryDrugs("PRENATAL/POSTPARTUM VIT/MIN");
        List<Drug> queryCurrentDrugResult = fdbPrescriberUnoptimized.queryDrugs("APO-QUIN-G 325 MG TABLET");
        queryCurrentDrugResult.add(fdbPrescriberUnoptimized.queryDrugs("BIO BALANCED CALC/MAG TAB").get(0));
        Patient patient = new Patient();
        patient.addDrug(queryCurrentDrugResult.get(0));
        patient.addDrug(queryCurrentDrugResult.get(1));
        List<DrugInteraction> queryDrugToDrugInteractionResult = fdbPrescriberUnoptimized.queryDrugInteractionsWithOtherDrugs(queryNewDrugResult.get(0),patient);
        String drugToDrugClinicalEffectTextResult = "";
        for(int i = 0; i < queryDrugToDrugInteractionResult.size(); i++){
            drugToDrugClinicalEffectTextResult += "(" + queryDrugToDrugInteractionResult.get(i).getInteractionDescription() + ")\n";
        }
        Assert.assertEquals(drugToDrugClinicalEffectTextResult, "(PRENATAL/POSTPARTUM VIT/MIN Decreased effect of the former drug BIO BALANCED CALC/MAG TAB)\n"
        +"(PRENATAL/POSTPARTUM VIT/MIN Mixed effects of the latter drug BIO BALANCED CALC/MAG TAB)\n"
        +"(PRENATAL/POSTPARTUM VIT/MIN Mixed effects of the latter drug APO-QUIN-G 325 MG TABLET)\n");
    }
}
