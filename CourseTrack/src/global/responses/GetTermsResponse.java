package global.responses;

import java.io.Serializable;

import global.LinkedList;
import global.data.Term;

public record GetTermsResponse(
    LinkedList<Term> terms
) implements Serializable { }
