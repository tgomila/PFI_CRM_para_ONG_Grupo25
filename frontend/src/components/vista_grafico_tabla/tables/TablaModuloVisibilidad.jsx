import React, { useMemo, useState, useEffect, useRef } from "react";
import UserService from "../../../services/UserService";
import { TablaGenerica } from "./Tabla_Generica";
import modulosService from "../../../services/modulosService";
import { required } from "../../CRUD/Constants/ConstantsInput";
import { 
  IndeterminateCheckbox, 
  RenderFotoPerfilRow,
  RenderBotonEditar,
  RenderBotonBorrar,
} from"./Tabla_Variables";
import {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
} from"./Tabla_Filters";

const TablaModuloVisibilidad = ({visibilidadInput}) => {
  const [data, setData] = useState(null);

  useEffect(() => {
    modulosService.getAllParaModificar().then((res) => {
      setData(res.data);
      console.log("Data de la tabla");
      console.log(res.data);
    });
  }, []);

  const handleVisibilidadChange = async (event, row) => {
    const newVisibilidad = event.target.value;

    try {
      console.log("row");
      console.log(row);
      const response = await modulosService.putModificarModuloVisibilidad(row.original.roleEnum, row.original.moduloEnum, newVisibilidad);
      if (response && response.tipoVisibilidadEnum) {
        // row.original.tipoVisibilidadEnum = response.tipoVisibilidadEnum;
        // setData([...data]);

        // Crear una copia del estado data
        const updatedData = [...data];
        // Encontrar el índice del objeto modificado
        const dataIndex = updatedData.findIndex(
          (item) =>
            item.roleEnum === row.original.roleEnum &&
            item.moduloEnum === row.original.moduloEnum
        );
        // Actualizar el valor en la copia
        updatedData[dataIndex].tipoVisibilidadEnum =
          response.tipoVisibilidadEnum;

        // Actualizar el estado
        setData(updatedData);
      }
    } catch (error) {
    }
  };

  const columns = useMemo(() => {
    let baseColumns = [
      {
        Header: "Rol",
        accessor: "roleName",
        Filter: SelectColumnFilter,
        filter: 'equals',
        type: "string",
        Cell: ({ value, row }) => {
          return (
            <div>
              <div style={{ display: "none" }}>{value}</div>
              {row.original.roleName}
            </div>
          );
        },
      },
      {
        Header: "Modulo",
        accessor: "moduloName",
        Filter: SelectColumnFilter,
        filter: 'equals',
        type: "string",
        Cell: ({ value, row }) => {
          return (
            <div>
              <div style={{ display: "none" }}>{value}</div>
              {row.original.moduloName}
            </div>
          );
        },
      },
      {
        Header: "Visibilidad del modulo para el rol",
        accessor: "tipoVisibilidadEnum",
        Filter: SelectColumnFilter,
        filter: 'includes',
        type: "string",
        Cell: ({ value, row }) => {
          const [valueSaved, setValueSaved] = useState(value);
          const [isLoading, setIsLoading] = useState(false);

          const handleVisibilidadChange = async (event, row) => {
            const newVisibilidad = event.target.value;
        
            try {
              setIsLoading(true);
              const response = await modulosService.putModificarModuloVisibilidad(row.original.roleEnum, row.original.moduloEnum, newVisibilidad);
              if (response && response.tipoVisibilidadEnum) {
                row.original.tipoVisibilidadEnum = response.tipoVisibilidadEnum;
                setValueSaved(response.tipoVisibilidadEnum);
              }
              setIsLoading(false);
            } catch (error) {
              setIsLoading(false);
            }
          };

          return (
            <>
            {value === "SIN_SUSCRIPCION" ? (
              <label> Sin suscripción </label>
            ) : (
              <>
              <select name="tipoVisibilidadName" 
                value={valueSaved} 
                onChange={(event) => handleVisibilidadChange(event, row)} 
                className="form-control" 
                validations={[required]}
              >
                  <option value={"NO_VISTA"}>No vista</option>
                  <option value={"SOLO_VISTA"}>Solo vista</option>
                  <option value={"EDITAR"}>Editar</option>
                  {/* <option value={"SIN_SUSCRIPCION"}>Sin suscripción</option> */}
              </select>
              {isLoading && (
                <div>
                  <span className="spinner-border spinner-border-sm"></span>
                  <p>Modificando...</p>
                </div>
              )}
              </>
            )}
            </>
          );
        },
      },
    ];
    console.log("visibilidad: " + visibilidadInput);

  return baseColumns;
  }, [visibilidadInput]);
  
  return(//{columnsIn, data, visibilidad, Service, el_la, nombreTipoDato}) => {
    <div>
      <div className="Marketplace">
        <h1><span className="underlined underline-clip-subtitle">Visibilidad de módulos</span></h1>
        <br></br>
      </div>
      {data && (
        <TablaGenerica
          columnsIn={columns}
          dataIn={data}
          Service={null}
          visibilidadInput={null}
          nombreTipoDatoParaModuloVisibilidad={"MARKETPLACE"}
          el_la={"el"}
          nombreTipoDato={"módulo"}
        />
      )}
    </div>
  );
}

export default TablaModuloVisibilidad;
