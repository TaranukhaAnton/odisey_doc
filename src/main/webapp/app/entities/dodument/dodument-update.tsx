import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IDodument } from 'app/shared/model/dodument.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './dodument.reducer';

export const DodumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const dodumentEntity = useAppSelector(state => state.dodument.entity);
  const loading = useAppSelector(state => state.dodument.loading);
  const updating = useAppSelector(state => state.dodument.updating);
  const updateSuccess = useAppSelector(state => state.dodument.updateSuccess);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/dodument' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...dodumentEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          date: displayDefaultDateTime(),
        }
      : {
          status: 'DRAFT',
          ...dodumentEntity,
          date: convertDateTimeFromServer(dodumentEntity.date),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="odiseyDocApp.dodument.home.createOrEditLabel" data-cy="DodumentCreateUpdateHeading">
            <Translate contentKey="odiseyDocApp.dodument.home.createOrEditLabel">Create or edit a Dodument</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="dodument-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('odiseyDocApp.dodument.description')}
                id="dodument-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('odiseyDocApp.dodument.actionsDescription')}
                id="dodument-actionsDescription"
                name="actionsDescription"
                data-cy="actionsDescription"
                type="text"
              />
              <ValidatedField
                label={translate('odiseyDocApp.dodument.date')}
                id="dodument-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('odiseyDocApp.dodument.status')}
                id="dodument-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('odiseyDocApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/dodument" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default DodumentUpdate;
