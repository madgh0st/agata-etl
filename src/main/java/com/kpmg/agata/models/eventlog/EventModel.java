package com.kpmg.agata.models.eventlog;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.kpmg.agata.models.clean.AbstractCleanDataModel;
import com.kpmg.agata.models.clean.CleanCounterpartyContractModel;
import com.kpmg.agata.models.clean.CleanCreditCorrectionModel;
import com.kpmg.agata.models.clean.CleanCreditLimitModel;
import com.kpmg.agata.models.clean.CleanExpressAnalysisModel;
import com.kpmg.agata.models.clean.CleanFnsArrearsModel;
import com.kpmg.agata.models.clean.CleanFnsDisqModel;
import com.kpmg.agata.models.clean.CleanFnsTaxViolationModel;
import com.kpmg.agata.models.clean.CleanFsspModel;
import com.kpmg.agata.models.clean.CleanGenprocModel;
import com.kpmg.agata.models.clean.CleanLegalCasesModel;
import com.kpmg.agata.models.clean.CleanPaymentFromCustomerModel;
import com.kpmg.agata.models.clean.CleanRealizationServicesModel;
import com.kpmg.agata.models.clean.CleanSectoralIndexModel;
import com.kpmg.agata.models.clean.CleanSparkInterfaxSummaryModel;
import com.kpmg.agata.models.clean.CleanWeeklyStatusPDZFlatModel;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

public class EventModel implements Serializable {
    private String code;
    private String name_do;
    private String eventType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+3")
    private Date eventDate;

    @JsonTypeInfo(use = Id.NAME, property = "eventType")
    @JsonSubTypes({
            @Type(name = "CreditCorrection", value = CleanCreditCorrectionModel.class),
            @Type(name = "CreditLimit", value = CleanCreditLimitModel.class),
            @Type(name = "ExpressAnalysis", value = CleanExpressAnalysisModel.class),
            @Type(name = "LegalCases", value = CleanLegalCasesModel.class),
            @Type(name = "PaymentFromCustomer", value = CleanPaymentFromCustomerModel.class),
            @Type(name = "RealizationServices", value = CleanRealizationServicesModel.class),
            @Type(name = "WeeklyStatusPDZFlat", value = CleanWeeklyStatusPDZFlatModel.class),
            @Type(name = "SparkInterfaxSummary", value = CleanSparkInterfaxSummaryModel.class),
            @Type(name = "FnsTaxViolation", value = CleanFnsTaxViolationModel.class),
            @Type(name = "FnsArrears", value = CleanFnsArrearsModel.class),
            @Type(name = "Genproc", value = CleanGenprocModel.class),
            @Type(name = "FnsDisq", value = CleanFnsDisqModel.class),
            @Type(name = "Fssp", value = CleanFsspModel.class),
            @Type(name = "SectoralIndex", value = CleanSectoralIndexModel.class),
            @Type(name = "CounterpartyContract", value = CleanCounterpartyContractModel.class)
    })
    private AbstractCleanDataModel data;

    public EventModel() {
    }

    public EventModel(String code, String name_do, Date eventDate, AbstractCleanDataModel data) {
        this.code = code;
        this.name_do = name_do;
        this.eventType = getEventType(data);
        this.eventDate = eventDate;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public AbstractCleanDataModel getData() {
        return data;
    }

    public void setData(AbstractCleanDataModel data) {
        this.data = data;
    }

    public String getName_do() {
        return name_do;
    }

    public void setName_do(String name_do) {
        this.name_do = name_do;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventModel that = (EventModel) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name_do, that.name_do) &&
                Objects.equals(eventType, that.eventType) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name_do, eventType, eventDate, data);
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "code='" + code + '\'' +
                ", name_do='" + name_do + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventDate=" + eventDate +
                ", data=" + data +
                '}';
    }


    private String getEventType(AbstractCleanDataModel model) {
        return model.getClass()
                .getName()
                .replaceAll("com\\.kpmg\\.agata\\.models\\.clean\\.", "")
                .replace("Model", "")
                .replace("Clean", "")
                .trim();
    }
}
