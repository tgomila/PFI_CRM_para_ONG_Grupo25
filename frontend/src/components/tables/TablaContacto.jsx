import React, { forwardRef, useMemo, useState, useEffect, useRef } from "react";
import ContactoService from "../../services/ContactoService";
import ImageService from "../../services/ImageService";

import { useTable, usePagination, useSortBy, useRowState } from "react-table";

import "../../Styles/Tabla.scss";

import { useNavigate } from 'react-router-dom';

//Para que funcione el tooptip (Button a la derecha)
import { Modal, Button, OverlayTrigger, Tooltip } from "react-bootstrap";
//import "../../../node_modules/bootstrap/dist/css/bootstrap.min.css";
import { FaTrashAlt, FaRegEdit } from "react-icons/fa";
//import ReactToolTip from 'react-tooltip';
import IndeterminateCheckbox from"./Tabla_Variables";


import {
  Route,
  Routes,
  BrowserRouter
} from "react-router-dom";



function TablaContacto() {
  const [data, setData] = useState([]);
  const [fotoPerfil, setFotoPerfil] = useState(null);
  //const [columnNames, setColumnNames] = useState([]);

  useEffect(() => {

    ContactoService.getAll().then((res) => {
      setData(res.data);
      console.log(res.data);
    });

    ContactoService.getAll().then(async (res) => {
      const newData = [];
    
      for (const contacto of res.data) {
        const fotoUrl = await ImageService.getFotoContacto(contacto.id);
        const contactoConFoto = { ...contacto, imagen: fotoUrl };
        newData.push(contactoConFoto);
      }
    
      setData(newData);
      console.log(newData);
    });

    /*const fetchFotoPerfil = ImageService.getFotoPerfilNew();
    if(fetchFotoPerfil) {
      console.log("foto");
      console.log(fetchFotoPerfil);
      setFotoPerfil(fetchFotoPerfil);
    }*/

    
    const fetchFotoPerfil = async () => {
      try {
        //const data = await ImageService.getFotoPerfil();
        //const blob = new Blob([data]);
        //const fotoUrl = URL.createObjectURL(blob);
        //setFotoPerfil(fotoUrl);
        const fotoUrl = await ImageService.getFotoPerfil();
        setFotoPerfil(fotoUrl);
      } catch (error) {
        console.error(error);
      }
    };

    fetchFotoPerfil();
    
   

    //ImageService.getFotoPerfil().then((res) => {
    //  setFotoPerfil(res.data);
    //});

    //ContactoService.getColumnNames().then((res) => {
    //  setColumnNames(res.data);
    //});

  }, []);

  //const columns = Object.entries(columnNames || []).map(([key,value]) => ({
  //  Header: value.toString(),
  //  accessor: key,
  //}));
  const columns = useMemo(
    () => [
    {
      Header: "ID",
      accessor: "id",
    },
    {
      Header: "Foto",
      Cell: ({ row }) => renderFotoPerfil(row.original.imagen),
    },
    {
      Header: "Nombre/Descripción",
      accessor: "nombreDescripcion",
    },
    {
      Header: "Email",
      accessor: "email",
    },
    {
      Header: "Cuit",
      accessor: "cuit",
    },
    {
      Header: "Telefono",
      accessor: "telefono",
    },
    {
      Header: 'Editar',
      Cell: ({ row }) => renderBotonEditar(row.original.id),
    },
    {
      Header: 'Borrar',
      Cell: ({ row }) => renderBotonBorrar(row.original.id),
    },
  ],
  []);

  const columns2 = useMemo(
    () => [
      {
        // first group - Contacto
        Header: "Datos de contactos",
        // First group columns
        columns: [
          {
            Header: "ID",
            accessor: "id",
          },
          {
            Header: "Nombre/Descripción",
            accessor: "nombreDescripcion",
          },
          {
            Header: "Email",
            accessor: "email",
          },
          {
            Header: "Cuit",
            accessor: "cuit",
          },
          {
            Header: "Telefono",
            accessor: "telefono",
          },
          {
            Header: 'Editar',
            Cell: ({ row }) => renderBotonEditar(row.original.id),
          },
          {
            Header: 'Borrar',
            Cell: ({ row }) => renderBotonBorrar(row.original.id),
          },
        ],
      },
      /*{
        // Second group - Persona
        Header: "Datos de persona",
        // Second group columns
        columns: [
          {
            Header: "Nombre",
            accessor: "nombreDescripcion",
          },
        ],
      },*/
    ],
    []
  );


  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    page,
    nextPage,
    previousPage,
    canPreviousPage,
    canNextPage,
    pageOptions,
    state,
    gotoPage,
    pageCount,
    setPageSize,
    prepareRow,
    allColumns,
    getToggleHideAllColumnsProps,
  } = useTable(
    {
      columns,
      data,
      initialState: { pageIndex: 0 },
    },
    useSortBy,
    usePagination,
    useRowState
  );

  const { pageIndex, pageSize } = state;
  window.scrollTo({ top: 0, behavior: "smooth" });

  
  const [modalOpen, setModalOpen] = useState(false);
  const [idAux, setIdAux] = useState();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const changeTrueModalOpen = (row) => {
    setModalOpen(!modalOpen);
    setIdAux(row);
    console.log(row.values.id);
  };

  const changeFalseModalOpen = () => {
    setModalOpen(false);
    setIdAux();
  };

  const eliminarRegistro = (id) => {
    if(id != null){

      setMessage("");
      setLoading(true);
      ContactoService.delete(id).then(
        () => {
          setLoading(false);
          window.location.reload();
        },
        (error) => {
          const resMessage =
            (error.response &&
              error.response.data &&
              error.response.data.message) ||
            error.message ||
            error.toString();

          setLoading(false);
          setMessage(resMessage);
        });
    } else {
      setLoading(false);
    }
  }

  const renderBotonEditar = (idInput) => {
    return (
      <OverlayTrigger
        placement="top"
        overlay={
          <Tooltip id="tooltip-top">
            Editar ID: {idInput}
          </Tooltip>
        }
      >
        <Button
          className="buttonAnimadoVerde"
          onClick={() => navigate( window.location.pathname + "/update", {state:{id:idInput}})}
        >
          {" "}
          <FaRegEdit/>{/**Editar*/}
        </Button>
      </OverlayTrigger>
    );
  };

  const renderBotonBorrar = (idInput) => {
    return (
      <OverlayTrigger
        placement="top"
        overlay={
          <Tooltip id="tooltip-top">
            ¿Seguro desea <strong>borrar</strong> ID: {idInput} ?
          </Tooltip>
        }
      >
      <Button
        className="buttonAnimadoRojo"
        onClick={() => changeTrueModalOpen(idInput)}
      >
        <FaTrashAlt/>{/**Borrar*/}
      </Button>
      </OverlayTrigger>
    );
  };

  const renderFotoPerfil = (imagen) => {
    return(
      <div>
        {imagen ? (
        <img 
        src={imagen} 
        alt="Foto de perfil" 
        className="contacto-img-card"
        />
      ) : (
        <p>Cargando foto de perfil...</p>
      )}
      </div>

    );
  };


  // Render the UI for your table
  return (
    <div className="componentePrincipal">
      
      {/**Botones agregar quitar */}
      <div className="row">
        <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/create")}> Nuevo contacto</button>
        &nbsp;&nbsp;&nbsp;
        <button className="btn btn-primary" onClick={() => navigate( window.location.pathname + "/update")}> Modificar contacto</button>
      </div>

      {/**Test foto perfil */}
      <img
          src="//ssl.gstatic.com/accounts/ui/avatar_2x.png"
          alt="profile-img"
          className="profile-img-card"
      />
      {fotoPerfil ? (
        <img 
        src={fotoPerfil} 
        alt="Foto de perfil" 
        className="contacto-img-card"
        />
      ) : (
        <p>Cargando foto de perfil...</p>
      )}
      
      
      {/**Tabla */}
      <br></br>
      <div className="tablaVista">
        <div className="tituloNroColumns"> &nbsp; Seleccione columnas a mostrar/ocultar &nbsp;</div>
        <div className="row custom-row">
          <IndeterminateCheckbox {...getToggleHideAllColumnsProps()} />
          
          {allColumns.map((column) => (
            <div className="cb action" key={column.id}>
              <label>
                <input type="checkbox" {...column.getToggleHiddenProps()} />{" "}
                <span>{column.Header}</span>
              </label>
            </div>
          ))}
        </div>
      </div>
      <br/>
      <div className="row">
        <div>
          <div className="tablaVista">
            <table>
              <thead>
                {headerGroups.map((headerGroup) => (
                  <tr {...headerGroup.getHeaderGroupProps()}>

                    {headerGroup.headers.map((column, index) => (
                      <th {...column.getHeaderProps()} className={headerGroup.headers.length === 1 ? 'th-one' : index === 0 ? 'th-first' : index === headerGroup.headers.length - 1 ? 'th-last' : 'th'}>
                        {column.render("Header").toUpperCase()}
                      </th>
                    ))}
        
                  </tr>
                ))}
              </thead>

              <tbody {...getTableBodyProps()}>
                {page.map((row, i) => {
                  prepareRow(row);
                  return (
                    <tr {...row.getRowProps()}>
                      {row.cells.map((cell) => {
                        return (
                          <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                        );
                      })}

                    </tr>
                  );
                })}
              </tbody>
            </table>

            <div className="tablaButtomBarra">
              <button
                className="buttonAnimadoAzul"
                onClick={() => gotoPage(0)}
                disabled={!canPreviousPage}
              >
                {"<<"}
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => previousPage()}
                disabled={!canPreviousPage}
              >
                Anterior
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => nextPage()}
                disabled={!canNextPage}
              >
                Siguiente
              </button>{" "}
              <button
                className="buttonAnimadoAzul"
                onClick={() => gotoPage(pageCount - 1)}
                disabled={!canNextPage}
              >
                {">>"}
              </button>{" "}
              <span>
                Página{" "}
                <strong>
                  {pageIndex + 1} de {pageOptions.length}
                </strong>{" "}
              </span>
              <span>
                | Ir a la Página:{" "}
                <input
                  type="number"
                  defaultValue={pageIndex + 1}
                  onChange={(e) => {
                    const pageNumber = e.target.value
                      ? Number(e.target.value) - 1
                      : 0;
                    gotoPage(pageNumber);
                  }}
                  style={{ width: "50px" }}
                />
              </span>{" "}
              <span className="miDivSelect">
                <span className="dropup">
                  <select
                    className="selectAnimado"
                    value={pageSize}
                    onChange={(e) => setPageSize(Number(e.target.value))}
                  >
                    {[10, 25, 50].map((pageSize) => (
                      <option
                        className="dropup-content"
                        key={pageSize}
                        value={pageSize}
                      >
                        Mostrar {pageSize}
                      </option>
                    ))}
                  </select>
                </span>
              </span>
            </div>
          </div>
          
          {/** Este es el cartel que aparece delante "desea borrar?" */}
          <div>
            <Modal
              show={modalOpen}
              onHide={() => changeTrueModalOpen()}
            >
              <Modal.Header closeButton>
                <Modal.Title>Deseas borrar al ID {idAux ? idAux : ""}?</Modal.Title>
              </Modal.Header>

              <Modal.Body>
                <p>Deseas borrar al ID {idAux ? idAux : ""}?</p>
              </Modal.Body>

              <Modal.Footer>
                <Button variant="secondary"
                  onClick={() => changeFalseModalOpen()}
                >CERRAR</Button>
                <Button variant="primary"
                  onClick={() => eliminarRegistro(idAux ? idAux : null)}
                >ELIMINAR</Button>
                {loading && (
                  <span className="spinner-border spinner-border-sm"></span>
                )}
              </Modal.Footer>
            </Modal>
          </div>



        </div>
      </div>
    </div>
  );
}

export default TablaContacto;
