<%-- 
    Document   : teste
    Created on : 22/04/2016, 22:36:45
    Author     : GUSTAVO-PC
--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
            <title>Asynchronous file Upload in Java Web Application</title>
            <script src="jquery-1.8.2.js"></script>
            <script src="jquery.ajaxfileupload.js"></script>
            <script language="Javascript">
                $(document).ready(function () {
                      $('input[type="file"]').ajaxfileupload({
                               url: "/estimulos/estimulo",
                        data: {
                            operacao: "SALVAR",
                            acao: "SALVAR"
                        },            
                           'onComplete': function (response) {         
                                     $('#upload').hide();
                                     alert("File SAVED!!");
                               },
                               'onStart': function () {
                                     $('#upload').show();
                               }
                      });
                });
            </script>
    </head>
    <body>
        <form>
            <div>                                 
                 <input type="file" name="datafile" />   
                 
                 <div id="upload" style="display:none;">Uploading..</div>
            </div>
        </form>
    </body>
</html>