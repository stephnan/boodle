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
     :modal.achieved-confirm "Confermi che la meta è stata felicemente raggiunta?"
     :modal.achieved-title "Meta raggiunta?"
     :modal.button-achieved "Archivia meta"
     :modal.button-delete "Cancella meta"
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

    :button/cancel "Annulla"
    :button/ok "Ok"
    :currency "€"
    :date.placeholder "dd/mm/yyyy"
    
    :expenses
    {:button.add "Aggiungi"
     :button.reset "Reset"
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
     :modal.item "Oggetto"
     :page.title "Spese"
     :table.actions "Azioni"
     :table.amount "Importo"
     :table.category "Categoria"
     :table.date "Data"
     :table.item "Oggetto"}

    :header
    {:savings "Risparmi"
     :boodle "boodle"
     :expenses "Spese"
     :report "Report"}

    :report
    {:button.search "Cerca spese"
     :label.category "Categoria"
     :label.from "Da"
     :label.item "Oggetto"
     :label.to "A"
     :label.total "Totale "
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
     :label.total "Totale "
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
