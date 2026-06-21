export default function StatusMessage({ loading, error, empty, children }) {
  if (loading) {
    return <div className="stateBox">Cargando informacion...</div>;
  }

  if (error) {
    return (
      <>
        <div className="stateBox stateNotice">{error}</div>
        {children}
      </>
    );
  }

  if (empty) {
    return <div className="stateBox">No hay registros disponibles.</div>;
  }

  return children;
}
