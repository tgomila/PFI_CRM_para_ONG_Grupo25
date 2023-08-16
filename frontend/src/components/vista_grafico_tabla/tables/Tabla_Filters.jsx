import React, { forwardRef, useMemo, useState, useEffect, useRef } from "react";
import { Modal, Button, OverlayTrigger, Tooltip } from "react-bootstrap";
import ImageService from "../../../services/ImageService";
import { useNavigate } from 'react-router-dom';
import { FaTrashAlt, FaRegEdit } from "react-icons/fa";

import { useTable, usePagination, useSortBy, useRowState, useFilters, useGlobalFilter, useAsyncDebounce } from "react-table";

import {matchSorter} from 'match-sorter'

import "../../../Styles/Tabla.scss";

//Origen de filtros:
//https://codesandbox.io/s/tannerlinsley-react-table-filtering-4tiuf?file=/src/App.js:1350-1386
//A futuro ver este:
//https://codesandbox.io/s/tannerlinsley-react-table-kitchen-sink-gw0ih?file=/src/App.js:17865-17875

// Define a default UI for filtering
function GlobalFilter({
  preGlobalFilteredRows,
  globalFilter,
  setGlobalFilter,
}) {
  const count = preGlobalFilteredRows.length
  const [value, setValue] = React.useState(globalFilter)
  const onChange = useAsyncDebounce(value => {
    setGlobalFilter(value || undefined)
  }, 200)

  return (
    <span>
      Buscador general:{' '}
      <input
        className='search-field'
        value={value || ""}
        onChange={e => {
          setValue(e.target.value);
          onChange(e.target.value);
        }}
        placeholder={`${count} resultados...`}
      />
    </span>
  )
}

// Define a default UI for filtering
const DefaultColumnFilter = ({
  column: { filterValue, preFilteredRows, setFilter },
}) => {
  const count = preFilteredRows.length

  return (
    <input
    className='search-field'
      value={filterValue || ''}
      onChange={e => {
        setFilter(e.target.value || undefined) // Set undefined to remove the filter entirely
      }}
      placeholder={`Buscar en ${count} resultados...`}
    />
  )
}

// This is a custom filter UI for selecting
// a unique option from a list
function SelectColumnFilter({
  column: { filterValue, setFilter, preFilteredRows, id },
}) {
  // Calculate the options for filtering
  // using the preFilteredRows
  const options = React.useMemo(() => {
    const options = new Set()
    preFilteredRows.forEach(row => {
      options.add(row.values[id])
    })
    return [...options.values()]
  }, [id, preFilteredRows])

  // Render a multi-select box
  return (
    <select
      value={filterValue}
      onChange={e => {
        setFilter(e.target.value || undefined)
      }}
    >
      <option value="">All</option>
      {options.map((option, i) => (
        <option key={i} value={option}>
          {option}
        </option>
      ))}
    </select>
  )
}

// This is a custom filter UI that uses a
// slider to set the filter value between a column's
// min and max values
function SliderColumnFilter({
  column: { filterValue, setFilter, preFilteredRows, id },
}) {
  // Calculate the min and max
  // using the preFilteredRows

  const [min, max] = React.useMemo(() => {
    let min = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
    let max = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
    preFilteredRows.forEach(row => {
      min = Math.min(row.values[id], min)
      max = Math.max(row.values[id], max)
    })
    return [min, max]
  }, [id, preFilteredRows])

  return (
    <div>
      {filterValue}
      <input
        type="range"
        min={min}
        max={max}
        value={filterValue || min}
        onChange={e => {
          setFilter(parseInt(e.target.value, 10))
        }}
      />
      <button onClick={() => setFilter(undefined)}>Off</button>
    </div>
  )
}

// This is a custom UI for our 'between' or number range
// filter. It uses two number boxes and filters rows to
// ones that have values between the two
function NumberRangeColumnFilter({
  column: { filterValue = [], preFilteredRows, setFilter, id },
}) {
  const [min, max] = React.useMemo(() => {
    let min = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
    let max = preFilteredRows.length ? preFilteredRows[0].values[id] : 0
    preFilteredRows.forEach(row => {
      min = Math.min(row.values[id], min)
      max = Math.max(row.values[id], max)
    })
    return [min, max]
  }, [id, preFilteredRows])

  return (
    <div>
      <div
        style={{
          display: 'flex',
        }}
      >
        <input
          value={filterValue[0] || ''}
          type="number"
          onChange={e => {
            const val = e.target.value
            setFilter((old = []) => [val ? parseInt(val, 10) : undefined, old[1]])
          }}
          placeholder={`Min (${min})`}
          style={{
            width: '70px',
            marginRight: '0.5rem',
          }}
        />
        a
        <input
          value={filterValue[1] || ''}
          type="number"
          onChange={e => {
            const val = e.target.value
            setFilter((old = []) => [old[0], val ? parseInt(val, 10) : undefined])
          }}
          placeholder={`Max (${max})`}
          style={{
            width: '70px',
            marginLeft: '0.5rem',
          }}
        />
        
      </div>
      <button className="btn btn-primary btn-sm mt-2" onClick={() => setFilter([])}>Limpiar</button>
    </div>
  )
}

const fuzzyTextFilterFn = (rows, id, filterValue) => {
  return matchSorter(rows, filterValue, { keys: [row => row.values[id]] })
};

const DateRangeFilter = ({ column }) => {
  const { filterValue = [], setFilter } = column;

  const handleStartDateChange = (e) => {
    const newFilterValue = [...filterValue];
    newFilterValue[0] = e.target.value;
    setFilter(newFilterValue);
  };

  const handleEndDateChange = (e) => {
    const newFilterValue = [...filterValue];
    newFilterValue[1] = e.target.value;
    setFilter(newFilterValue);
  };

  return (
    <div>
      Desde:{' '}
      <input
        value={filterValue[0] || ''}
        onChange={handleStartDateChange}
        placeholder="YYYY-MM-DD"
      />
      <br />
      Hasta:{' '}
      <input
        value={filterValue[1] || ''}
        onChange={handleEndDateChange}
        placeholder="YYYY-MM-DD"
      />
    </div>
  );
};

const dateBetweenFilterFn = (rows, id, filterValues) => {
  const sd = filterValues[0] ? new Date(filterValues[0]) : undefined;
  const ed = filterValues[1] ? new Date(filterValues[1]) : undefined;

  if (ed || sd) {
    return rows.filter((r) => {
      const cellDate = new Date(r.values[id]);

      if (ed && sd) {
        return cellDate >= sd && cellDate <= ed;
      } else if (sd) {
        return cellDate >= sd;
      } else {
        return cellDate <= ed;
      }
    });
  } else {
    return rows;
  }
};

const DateHourRangeColumnFilter = ({
  column,
}) => {
  return (
    <DateGenericRangeColumnFilter
      column={column}
      typeDate="datetime-local"
    />
  );
};

const DateRangeColumnFilter = ({
  column,
}) => {
  return (
    <DateGenericRangeColumnFilter
      column={column}
      typeDate="date"
    />
  );
};


//privado
const DateGenericRangeColumnFilter = ({
  column: { filterValue = [], preFilteredRows, setFilter, id }, typeDate
}) => {
  const [min, max] = React.useMemo(() => {
    let min = preFilteredRows.length
      ? new Date(preFilteredRows[0].values[id])
      : new Date(0);
    let max = preFilteredRows.length
      ? new Date(preFilteredRows[0].values[id])
      : new Date(0);

    preFilteredRows.forEach((row) => {
      const rowDate = new Date(row.values[id]);

      min = rowDate <= min ? rowDate : min;
      max = rowDate >= max ? rowDate : max;
    });

    return [min, max];
  }, [id, preFilteredRows]);

  return (
    <div>
      Desde:{' '}
      <input
        onChange={(e) => {
          const val = e.target.value;
          setFilter((old = []) => [val ? val : undefined, old[1]]);
        }}
        type={typeDate}
        value={filterValue[0] || ''}
      />
      {" hasta "}
      <input
        onChange={(e) => {
          const val = e.target.value;
          setFilter((old = []) => [
            old[0],
            val ? val : undefined,
          ]);
        }}
        type={typeDate}
        value={filterValue[1] || ''}
      />
      <button className="btn btn-primary btn-sm mt-2" onClick={() => setFilter([])}>Limpiar</button>
    </div>
  );
};





export {
  GlobalFilter,
  DefaultColumnFilter,
  SelectColumnFilter,
  SliderColumnFilter,
  fuzzyTextFilterFn,
  NumberRangeColumnFilter,
  dateBetweenFilterFn,
  DateRangeColumnFilter,
  DateHourRangeColumnFilter,
}
//export default IndeterminateCheckbox;
