import React, { useState } from 'react'


import { Modal, Button } from "react-bootstrap";





function Testing() {

  const [modalOpen, setModalOpen] = useState(false);

  const changeModalOpen = () => {
    setModalOpen(!modalOpen);
  };

  return (



    <div>
      <Button variant="primary" onClick={() => changeModalOpen()}>
        Click to hide/show
      </Button>
      <Modal
        show={modalOpen}
        onHide={() => changeModalOpen()}
      >
        <Modal.Header closeButton>
          <Modal.Title>This is modal title a</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>This is modal content</p>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary"
            onClick={() => changeModalOpen()}
          >CLOSE</Button>
          <Button variant="primary">SAVE</Button>
        </Modal.Footer>
      </Modal>
    </div>


  )
}

export default Testing