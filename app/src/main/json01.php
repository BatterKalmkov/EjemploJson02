<?php

$server = "localhost";
$user = "root";
$pass = "clave";
$bd = "guitars";

//Creamos la conexión
$conexion = mysqli_connect($server, $user, $pass,$bd)
or die("Ha sucedido un error inexperado en la conexion de la base de datos");

//generamos la consulta
$sql = "SELECT * FROM tabla_guitars";
mysqli_set_charset($conexion, "utf8"); //formato de datos utf8

if(!$result = mysqli_query($conexion, $sql)) die();

$guitars = array(); //creamos un array

while($row = mysqli_fetch_array($result))
{
    $marca=$row['marca'];
    $modelo=$row['modelo'];
    $cantidad=$row['cantidad'];


    $guitars[] = array('marca'=> $marca, 'modelo'=> $modelo, 'cantidad'=> $cantidad);

}

//desconectamos la base de datos
$close = mysqli_close($conexion)
or die("Ha sucedido un error inexperado en la desconexion de la base de datos");


//Creamos el JSON
$json_string = json_encode($guitars);
echo " { guitarras: ".$json_string." }";

//Si queremos crear un archivo json, sería de esta forma:
/*
$file = 'clientes.json';
file_put_contents($file, $json_string);
*/


?>
