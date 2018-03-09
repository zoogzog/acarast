package com.acarast.andrey.core.algorithm;

/**
 * Basic enumerate types to describe bacteria strains, drugs, processing mode e.t.c.
 * @author Andrey G
 *
 */
public class EnumTypes
{
    public enum BacteriaType {PAERUGINOSA};
    public static final String[] BacteriaTypeName = {"P. Aeruginosa"};
    public static final BacteriaType[] BacteriaTypeValue = {BacteriaType.PAERUGINOSA};
    
    public enum DrugType {AMIKACIN, CIPROFLOXACIN, MEROPENEM, CEFTAZIDIME, PIPERACILLIN};
    public static final String[] DrugTypeName = {"Amikacin", "Ciprofloxacin", "Meropenem", "Ceftazidime", "Piperacillin"}; 
    public static final DrugType[] DrugTypeValue = {DrugType.AMIKACIN, DrugType.CIPROFLOXACIN, DrugType.MEROPENEM, DrugType.CEFTAZIDIME, DrugType.PIPERACILLIN};
    
    public enum CriteriaType {MANUAL, SVM};
    
    //---- SPLIT_AND_MERGE - Algorithm developed by Andrey Grushnikov
    //---- GRAPH_MERGE - Algorithm developed by Kazuma Kikuchi
    public enum AlgorithmType {SPLIT_AND_MERGE, GRAPH_MERGE}
    public static final String[] AlgorithmTypeName = {"GraphMerge", "SplitMerge"};
    public static final AlgorithmType[] AlgorithmTypeValue = {AlgorithmType.GRAPH_MERGE, AlgorithmType.SPLIT_AND_MERGE};
}
