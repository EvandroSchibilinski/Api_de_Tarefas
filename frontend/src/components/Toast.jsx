export default function Toast({ mensagem, erro, visivel }) {
  return (
    <div className={`toast ${visivel ? 'show' : ''} ${erro ? 'erro' : ''}`}>
      {mensagem}
    </div>
  );
}
