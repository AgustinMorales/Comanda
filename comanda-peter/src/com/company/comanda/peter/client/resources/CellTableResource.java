package com.company.comanda.peter.client.resources;

import com.google.gwt.user.cellview.client.CellTable;

public interface CellTableResource extends CellTable.Resources 
{ 
   @Source(value = { CellTable.Style.DEFAULT_CSS, 
   "CellTable.css" }) 
   CellTable.Style cellTableStyle(); 
};