public enum Tipo {
    PSICOLOGIA,
    COMPUTACAO,
    SOCIOLOGIA,
    FISIOTERAPIA,
    PSICOTICO,
    ENGENHARIA,
    ARTE,
    VAGABUNDO,
    SAUDE;

    public double getEfetividadeContra(Tipo outroTipo) {
       
        if (this == PSICOLOGIA && outroTipo == COMPUTACAO) return 2.0; 
        if (this == COMPUTACAO && outroTipo == SOCIOLOGIA) return 2.0; 
        if (this == SOCIOLOGIA && outroTipo == PSICOLOGIA) return 2.0; 
        if (this == FISIOTERAPIA && outroTipo == SOCIOLOGIA) return 2.0; 
        if (this == PSICOTICO && outroTipo == PSICOLOGIA) return 2.0; 
        if (this == PSICOTICO && outroTipo == SOCIOLOGIA) return 2.0; 
        if (this == ENGENHARIA && outroTipo == COMPUTACAO) return 2.0;
        if (this == ARTE && outroTipo == FISIOTERAPIA) return 2.0; 

       
        if (this == VAGABUNDO && outroTipo == SOCIOLOGIA) return 2.0;
        if (this == SAUDE && outroTipo == FISIOTERAPIA) return 2.0; 

        
        if (this == COMPUTACAO && outroTipo == PSICOLOGIA) return 0.5; 
        if (this == SOCIOLOGIA && outroTipo == COMPUTACAO) return 0.5; 
        if (this == PSICOLOGIA && outroTipo == SOCIOLOGIA) return 0.5; 
        if (this == FISIOTERAPIA && outroTipo == COMPUTACAO) return 0.5; 
        if (this == PSICOTICO && outroTipo == COMPUTACAO) return 0.5; 
        if (this == ENGENHARIA && outroTipo == SOCIOLOGIA) return 0.5; 
        if (this == ARTE && outroTipo == PSICOTICO) return 0.5; 

        
        if (this == VAGABUNDO && outroTipo == COMPUTACAO) return 0.5; 
        if (this == SAUDE && outroTipo == PSICOTICO) return 0.5; 

        return 1.0; 
    }
}