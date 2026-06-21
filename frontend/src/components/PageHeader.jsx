export default function PageHeader({ title, description, actions }) {
  return (
    <div className="pageHeader">
      <div>
        <h2>{title}</h2>
        {description && <p>{description}</p>}
      </div>
      {actions && <div className="pageActions">{actions}</div>}
    </div>
  );
}
