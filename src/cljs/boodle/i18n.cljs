(ns boodle.i18n
  (:require [tongue.core :as tongue]))

(def dicts
  {:it
   {:aims
    {:button.achieved "Raggiunta"
     :button.add-transaction "Aggiungi movimento"
     :button.create "Crea meta"
     :label.achieved "Raggiunte"
     :label.active "Attive"
     :label.archive "Archivo"
     :label.total "Totale mete: "
     :message.category "Categoria: selezionare una categoria"
     :modal.achieved-title "Meta raggiunta?"
     :modal.button-achieved "Archivia meta"
     :modal.button-delete "Cancella meta"
     :modal.category "Seleziona una categoria per la meta"
     :modal.delete-confirm " Confermi la cancellazione della meta (movimenti inclusi)? "
     :modal.delete-title "Cancella meta"
     :modal.name "Nome"
     :modal.target "Obiettivo (€)"
     :page.title "Mete"
     :summary.table-actions "Azioni"
     :summary.table-aim "Meta"
     :summary.table-left "Rimanente"
     :summary.table-saved "Risparmiato"
     :summary.table-target "Obiettivo"}

    :button.cancel "Annulla"
    :button.ok "Ok"
    :currency "€"
    :date.placeholder "dd/mm/yyyy"

    :categories
    {:button.add "Crea categoria"
     :message.name "Nome: è obbligatorio"
     :message.new "Una nuova categoria è obbligatoria"
     :modal.button-delete "Cancella categoria"
     :modal.delete-confirm " Confermi la cancellazione della categoria? "
     :modal.delete-title "Cancella categoria"
     :modal.name "Nome"
     :modal.select-category "Seleziona una nuova categoria per le spese esistenti"
     :page.title "Categorie"
     :table.actions "Azioni"
     :table.name "Nome"}

    :expenses
    {:button.add "Aggiungi spesa"
     :button.reset "Reset ricerca"
     :button.search "Cerca spese"
     :label.category "Categoria"
     :label.from "Da"
     :label.to "A"
     :message.amount "Importo: deve essere un numero (es.: 3,55)"
     :message.category "Categoria: selezionare una categoria"
     :message.date "Data: deve rispettare il pattern dd/mm/yyyy"
     :message.from "Da: deve rispettare il pattern dd/mm/yyyy"
     :message.to "A: deve rispettare il pattern dd/mm/yyyy"
     :modal.amount "Importo (€)"
     :modal.button-delete "Cancella spesa"
     :modal.category "Categoria"
     :modal.date "Data"
     :modal.delete-confirm " Confermi la cancellazione della spesa? "
     :modal.delete-title "Cancella spesa"
     :modal.from-savings " Da risparmi"
     :modal.item "Oggetto"
     :page.title "Spese"
     :table.actions "Azioni"
     :table.amount "Importo"
     :table.category "Categoria"
     :table.date "Data"
     :table.from-savings "Da risparmi"
     :table.item "Oggetto"}

    :header
    {:boodle "boodle"
     :categories "Categorie"
     :report"Report"
     :savings "Risparmi"
     :expenses "Spese"}

    :pikaday
    {:previous-month "Mese precedente"
     :next-month "Mese successivo"
     :january "Gennaio"
     :february "Febbraio"
     :march "Marzo"
     :april "Aprile"
     :may "Maggio"
     :june "Giugno"
     :july "Luglio"
     :august "Agosto"
     :september "Settembre"
     :october "Ottobre"
     :november "Novembre"
     :december "Dicembre"
     :monday "Lunedì"
     :tuesday "Martedì"
     :wednesday "Mercoledì"
     :thursday "Giovedì"
     :friday "Venerdì"
     :saturday "Sabato"
     :sunday "Domenica"
     :mon "Lun"
     :tue "Mar"
     :wed "Mer"
     :thu "Gio"
     :fri "Ven"
     :sat "Sab"
     :sun "Dom"}

    :report
    {:button.search "Cerca spese"
     :label.category "Categoria"
     :label.from "Da"
     :label.from-savings " Da risparmi"
     :label.item "Oggetto"
     :label.to "A"
     :label.total "Totale: "
     :message.from "Da: deve rispettare il pattern dd/mm/yyyy"
     :message.to "A: deve rispettare il pattern dd/mm/yyyy"
     :page.title "Report"
     :table.amount "Amount"
     :table.category "Categoria"
     :table.date "Data"
     :table.item "Oggetto"}

    :savings
    {:button.add "Aggiungi risparmio"
     :button.transfer "Trasferisci importo"
     :label.active-aims "Mete attive"
     :label.total "Totale risparmi: "
     :label.total-unassigned "Totale non assegnati: "
     :message.aim "Meta: selezionare una meta"
     :message.amount "Importo: deve essere un numero (es.: 3,55)"
     :message.item "Motivo: è obbligatorio"
     :message.name "Nome: è obbligatorio"
     :message.target "Obiettivo: deve essere un numero (es.: 3,55)"
     :modal.amount "Importo"
     :modal.item "Motivo"
     :page.title "Risparmi"
     :table.amount "Importo"
     :table.date "Data"
     :table.item "Motivo"}

    :transactions
    {:label.left "Rimanente: "
     :label.saved "Risparmiato: "
     :label.target "Obiettivo: "
     :message.amount "Importo: deve essere un numero (es.: 3,55)"
     :message.item "Motivo: è obbligatorio"
     :modal.amount "Importo (€)"
     :modal.item "Motivo"
     :table.amount "Importo"
     :table.date "Data"
     :table.item "Motivo"}}})

(def translate (tongue/build-translate dicts))
