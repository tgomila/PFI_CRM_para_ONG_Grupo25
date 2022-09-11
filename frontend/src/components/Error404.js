import React from 'react';

import styled from 'styled-components';
import './NoMatch.css';
const Wrapper = styled.div`
  margin-top: 1em;
  margin-left: 6em;
  margin-right: 6em;
`;

function Error404() {
  return (
    <div>
      
      <Wrapper>
      <h2 className="nomatch">Pagina no encontrada!</h2>
    </Wrapper>

    </div>
  )
}

export default Error404