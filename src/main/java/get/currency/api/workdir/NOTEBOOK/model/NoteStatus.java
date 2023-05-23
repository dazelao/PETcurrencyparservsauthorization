package get.currency.api.workdir.NOTEBOOK.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public enum NoteStatus {
    @JsonPropertyDescription("New")
    NEW,

    @JsonPropertyDescription("Closed")
    CLOSED,

    @JsonPropertyDescription("In progress")
    INPROGRESS,

    @JsonPropertyDescription("Postponed")
    POSTPONED

}