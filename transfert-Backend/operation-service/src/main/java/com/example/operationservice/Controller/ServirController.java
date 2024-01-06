package com.example.operationservice.Controller;

import com.example.operationservice.Enum.TransferStatus;
import com.example.operationservice.Model.Customer;
import com.example.operationservice.Model.Transfer;
import com.example.operationservice.Repository.TransferRepository;

public class ServirController {
    private TransferRepository transferRepository;

    public ServirController(TransferRepository transferRepository){
        this.transferRepository=transferRepository;
    }


    public void servieEspeceConsoleAgent(String tranferReference){
        //transfer dans l'etat 'a servir' ou etat 'deploque a servir' + j meme jour de debloquage
        Transfer transfer=new Transfer();
        Customer customer=new Customer();

        transfer=transferRepository.findById(tranferReference).orElse(null);
        if(transfer == null){
            System.out.println("Message Bloquant , transfer n'existe pas");
            return ;
        }
        if(transfer.getStatus()== TransferStatus.SERVIE){
            System.out.println("Message Bloquant , transfer deja paye");
            return ;
        }
        //client black list dans sirone
        


        //agent saisie reference transfert
        //si exist : envoie des info de donneur d'ordre et de operation (montant,nom-prenom de beneficaire)

        //verifie le benificaire dans KYC

        //valide le paiment de transfert
            //mettre  a jour le solde de agent , commission ?

            //transfer est rendu 'paye' (servie)

            //Edition du reçu de paiement du transfert pour confirmer la réception de l’argent par le bénéficiaire

        //Le donneur d’ordre est notifié s’il a choisi l’option de «notification du transfert»

       // Un message bloquant doit être affiché Si:
       // ▪ La référence du transfert saisie est inexistante.
       // ▪ Le transfert est payé ou bloqué.
       // ▪ Le client bénéficiaire est black listé dans SIRONE.
    }
    public void servieWalletConsoleAgent(){
        //transfer dans l'etat 'a servir' ou etat 'deploque a servir' + j meme jour de debloquage

    }
    public void servieEspeceGAB(){
        //transfer dans l'etat 'a servir' ou etat 'deploque a servir' + j meme jour de debloquage
    }
}
